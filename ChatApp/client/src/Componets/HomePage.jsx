import React, { useEffect, useState } from 'react'
import { TbCircleDashed } from 'react-icons/tb';
import { BiCommentDetail } from 'react-icons/bi';
import { AiOutlineSearch } from 'react-icons/ai';
import { BsEmojiSmile, BsFilter, BsMicFill, BsThreeDotsVertical } from 'react-icons/bs';
import ChatCard from './ChatCard/ChatCard';
import MessageCard from './MessageCard/MessageCard';
import { ImAttachment } from 'react-icons/im';
import "./HomePage.css"
import { useNavigate } from 'react-router-dom';
import Profile from './Profile/Profile';
import { Menu, MenuItem } from '@mui/material';
import CreateGroup from './Group/CreateGroup';
import { useDispatch, useSelector } from 'react-redux';
import { currentUser, logoutAction, searchUser } from '../Redux/Auth/Action';
import { createChat, getUsersChat } from '../Redux/Chat/Action';
import { createMessage, getAllMessages } from '../Redux/Message/Action';

import SockJS from "sockjs-client/dist/sockjs";
import { over } from "stompjs";

const HomePage = () => {
    const [stompClient, setStompClient] = useState();
    const [isConnect, setIsConnect] = useState(false);
    const [messages, setMessages] = useState(null);
    const connect = () => {
        const sock = new SockJS("http://localhost:8080/ws");
        const temp = over(sock);
        setStompClient(temp);

        const headers = {
            Authotization: `Bearer ${token}`,
            "X-XSRF-TOKEN": getCookie("XSRF-TOKEN")
        }

        temp.connect(headers, onConnect, onError);
    }
    function getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) {
            return parts.pop().split(";").shift();
        }
    }
    const onError = (error) => {
        console.log("no error", error);
    }
    const onConnect = () => {
        setIsConnect(true);
    }
    const [querys, setQuerys] = useState(null);
    const [currentChat, setCurrentChat] = useState(null);
    const [content, setContent] = useState("");
    const [isProfile, setIsProfile] = useState(false);
    const navigate = useNavigate();
    const [isGroup, setIsGroup] = useState(false);

    const [anchorEl, setAnchorEl] = useState(null);
    const { auth, chat, message } = useSelector(store => store);
    const dispatch = useDispatch();
    const token = localStorage.getItem("token");
    
    const open = Boolean(anchorEl);
    
    const handleClick = (e) => {
        setAnchorEl(e.currentTarget);
    };
    const handleClose = () => {
        setAnchorEl(null);
    };

    const handleClickOnChatCard = (userId) => { 
        dispatch(createChat({ token, data:{userId}})) 
        setQuerys("")
    };

    const handleSearch = (keyword) => {
        dispatch(searchUser({ keyword, token }))
    }
    
    const handleCurrentChat = (item) => {
        setCurrentChat(item)
    };
    console.log("current chat",currentChat)

    const handleCreateNewMessage = () => {
        console.log("Create new Message")
        dispatch(createMessage({
            token,
            data: { chatId: currentChat.id, content: content }
        }))
    };
    
    useEffect(() => {
        dispatch(getUsersChat({ token }))
    }, [chat.createChat, chat.createGroup]);

    useEffect(() => {
        if (currentChat?.id)
            dispatch(getAllMessages({ chatId:currentChat.id, token }))
    }, [currentChat],message.newMessage);

    useEffect(() => {
        dispatch(getUsersChat({token}))
    },[chat.createChat, chat.createGroup]);

    const handleNavigate = () => {
        setIsProfile(true);
    };
    const handleCloseOpenProfile = () => {
        setIsProfile(false);
    };
    const handleCreateGroup = () => {
        setIsGroup(true)
    };
    useEffect(() => {
        dispatch(currentUser(token))
    }, [token])
    const handleLogout = () => {
        dispatch(logoutAction())
        navigate("/signup")
    }
    useEffect(() => {
        if (!auth.reqUser) {
            navigate("/signup")
        }
    }, [auth.reqUser])
    useEffect(() => {
        if (message.newMessage && stompClient) {
            setMessages([...messages, message.newMessage])
            stompClient?.send("/app/message", {}, JSON.stringify(message.newMessage));
        }
    }, message.newMessage)

    const onMessageRecive=(payload)=>{
        console.log("recive message ", JSON.parse(payload.body))
        const recivedMessage=JSON.parse(payload.body)
        setMessages([messages, recivedMessage]);
    }

    useEffect(()=>{
        if(isConnect && stompClient && auth.reqUser && currentChat){
            const subscription = stompClient.subscribe("/group/" + currentChat.id.toString, onMessageRecive)
        
            return ()=>{
                subscription.unsubcribe();
            }
        }
    })

    useEffect(()=>{
        connect();
    },[])

    useEffect(()=>{
        setMessages(message.messages)
    },message.messages)

    

    return (
        <div className='relative'>
            <div className='w-full py-14 bg-[#00a884]'></div>
            <div className='flex bg-[#f0f2f5] h=[90vh] absolute left-[2vw] top-[5vh] w-[96vw]'>
                <div className='left w-[30%] bg-[#e8e9ec] h-full'>
                    {/* profile */}
                    {isGroup && <CreateGroup setIsGroup={setIsGroup} />}
                    {isProfile && <div className='w-full h-full'>
                        <Profile handleCloseOpenProfile={handleCloseOpenProfile} /></div>}

                    {!isProfile && !isGroup && <div className='w-full'>

                        {/* profile */}
                        {isProfile && <div className='w-full h-full'><Profile /></div>}

                        {/* home */}

                        {<div className='flex justify-between items-center p-3'>
                            <div onClick={handleNavigate} className='flex items-center space-x-3'>
                                <img
                                    src={auth.reqUser?.profile_picture || "https://cdn.pixabay.com/photo/2024/02/06/13/33/flower-8557060_960_720.jpg"} alt=""
                                    className='rounded-full w-10 h-10 cursor-pointer' />
                                <p>{auth.reqUser?.full_name}</p>
                            </div>
                            <div className='space-x-3 text-2xl flex'>
                                <TbCircleDashed className='cursor-pointer'
                                    onClick={() => navigate("/status")} />
                                <BiCommentDetail />
                                <div>
                                    <BsThreeDotsVertical
                                        className='cursor-pointer'
                                        id="basic-button"
                                        aria-controls={open ? 'basic-menu' : undefined}
                                        aria-haspopup="true"
                                        aria-expanded={open ? 'true' : undefined}
                                        onClick={handleClick}
                                    />


                                    <Menu
                                        id="basic-menu"
                                        anchorEl={anchorEl}
                                        open={open}
                                        onClose={handleClose}
                                        MenuListProps={{
                                            'aria-labelledby': 'basic-button',
                                        }}
                                    >
                                        <MenuItem onClick={handleClose}>Profile</MenuItem>
                                        <MenuItem onClick={handleCreateGroup}>Create Group</MenuItem>
                                        <MenuItem onClick={handleLogout}>Logout</MenuItem>
                                    </Menu>
                                </div>

                            </div>
                        </div>}

                        <div className='relative  flex justify-center items-center bg-white py-4 px-3'>
                            <input className='border-none outline-none bg-slate-200 rounded-md w-[93%] pl-9 py-2'
                                type="text"
                                placeholder='Search or start a new Chat'
                                onChange={(e) => {
                                    setQuerys(e.target.value)
                                    handleSearch(e.target.value)
                                }}
                                value={querys}
                            />

                            <AiOutlineSearch className='left-5 top-7 absolute' />
                            <div>
                                <BsFilter className='ml-4 text-3xl' />
                            </div>
                        </div>
                        {/* all user */}
                        <div className='bg-white overflw-y-scroll h-[72vh] px-3'>
                            {querys && 
                                auth.searchUser?.map((item) => (
                                <div onClick={() => handleClickOnChatCard(item.id)}>
                                    {" "}
                                    <hr /> <ChatCard
                                            name={item.full_name}
                                            userImg={
                                                item.profile_picture ||
                                                "https://cdn.pixabay.com/photo/2024/02/06/13/33/flower-8557060_960_720.jpg"
                                            }
                                        />
                                </div>))}
                                {chat.chats.length>0 && !querys && 
                                chat.chats?.map((item) => (
                                <div onClick={() => handleCurrentChat(item)}> 
                                    <hr /> {item.is_group ? (
                                        <ChatCard 
                                            name={item.chat_name}
                                            userImg={
                                                item.chat_image ||
                                                "https://cdn.pixabay.com/photo/2024/02/06/13/33/flower-8557060_960_720.jpg"
                                            }
                                        />
                                    ):(
                                        <ChatCard 
                                            isChat={true}
                                            name={
                                                auth.reqUser?.id !== item.users[0]?.id
                                                ?item.users[0]?.full_name
                                                :item.users[1]?.full_name
                                            }
                                            userImg={
                                                auth.reqUser?.id !== item.users[0].id
                                                ?item.users[0].profile_picture ||
                                                "https://cdn.pixabay.com/photo/2024/02/06/13/33/flower-8557060_960_720.jpg"
                                                :item.users[1].profile_picture ||
                                                "https://cdn.pixabay.com/photo/2024/02/06/13/33/flower-8557060_960_720.jpg"
                                            }
                                        />
                                    )
                                    }
                                </div>
                            ))}
                        </div>
                    </div>
                    }
                </div>


                {/* default whats up page page */}
                {!currentChat && <div className='w-[70%] flex flex-col items-center justify-center h-full '>
                    <div className='max-w-[70%] text-center'>
                        <img src="https://cdn.pixabay.com/photo/2020/09/15/06/59/laptop-5572873_1280.png" alt="" />
                        <h1 className='text-4xl text-gray-600'> ChatsApp Web</h1>
                        <p className='my-9'>send and reveive message without keeping your phone online. Use ChatsApp on Up to 4 Linked devices and 1 phone at the same time.</p>

                    </div>
                </div>
                }
                {/*{message part}*/}

                {currentChat && (
                <div className='w-[70%] relative bg-blue-200'> 
                    <div className='header absolute top-0 w-full bg-[#f0f2f5]'>
                        <div className='flex justify-between'>
                            <div className='py-3 space-x-4 flex items-center px-3'>
                                <img className='w-10 h-10 rounded-full'
                                    src={currentChat.is_group ? currentChat.chat_image : (auth.reqUser?.id !== currentChat.users[0].id
                                        ?currentChat.users[0].profile_picture ||
                                        "https://cdn.pixabay.com/photo/2024/02/06/13/33/flower-8557060_960_720.jpg"
                                        :currentChat.users[1].profile_picture ||
                                        "https://cdn.pixabay.com/photo/2024/02/06/13/33/flower-8557060_960_720.jpg")}
                                    alt="" />
                                <p>
                                    {currentChat.is_group ? currentChat.chat_name : (auth.reqUser?.id === currentChat.users[0].id?currentChat.users[1].full_name:currentChat.user[0].full_name)}
                                </p>
                            </div>
                            <div className='py-3 flex space-x-4 items-center px-3'>
                                <AiOutlineSearch />
                                <BsThreeDotsVertical />
                            </div>
                        </div>
                    </div>
                    {/*message section */}
                    <div className=' px-10 h-[85vh] overflow-y-scroll bg-blue-200 '>
                        <div className='space-y-1 flex flex-col justify-center mt-20 py-2'>
                            {message.messages.length > 0 && message.messages?.map((item, i) => (
                                <MessageCard 
                                    isReqUserMessage={item.user.id !== auth.reqUser.id} 
                                    content={item.content} />))}
                        </div>
                    </div>
                    {/* footer part */}
                    <div className='footer bg-[#f0f2f5] absolute bottom-0 w-full py-3 text-2xl'>
                        <div className='flex justify-between items-center px-5 relative'>

                            <BsEmojiSmile className='cursor-pointer' />
                            <ImAttachment />

                            <input className='py-2 outline-none border-none bg-white pl-4 rounded-md w-[85%]' type='text' onChange={(e) => setContent(e.target.value)} placeholder='Type message' value={content} onKeyPress={(e) => {
                                if (e.key === "Enter") {
                                    handleCreateNewMessage();
                                    setContent("")
                                }
                            }} />
                            <BsMicFill />
                        </div>

                    </div>
                </div>)}
            </div>
        </div>
    )
}

export default HomePage