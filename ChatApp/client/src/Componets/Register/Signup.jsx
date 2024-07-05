import { Alert, Button, Snackbar } from '@mui/material';
import { green } from '@mui/material/colors';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { currentUser, register } from '../../Redux/Auth/Action';
import { useDispatch, useSelector } from 'react-redux';

const Signup = () => {
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const navigate = useNavigate();
    const [inputData, setInputData] = useState({ full_name: "", email: "", password: "" });
    const { auth } = useSelector(store => store);
    const token = localStorage.getItem("token");

    const dispatch = useDispatch();

    console.log('curent user', auth.reqUser)
    
    const handleSubmit = (e) => {
        e.preventDefault();
        console.log("handle submit", inputData);
        dispatch(register(inputData));
        setOpenSnackbar(true);
    };
    const handleChange = (e) => {
        const { name, value } = e.target;
        setInputData((values) => ({ ...values, [name]: value }));
    };
    const handleSnackbarClose = () => {
        setOpenSnackbar(false);
    };
    useEffect(() => {
        if (token)dispatch(currentUser(token))
    }, [token])
    useEffect(() => {
        if (auth.reqUser?.full_name) {
            navigate("/")
        }
    }, [auth.reqUser])
    return (
        <div>
            <div>
                <div className='flex flex-col justify-center min-h-screen items-center'>
                    <div className='w-[30%] p-10 shadow-md bg-white'>
                        <form onSubmit={handleSubmit} className='space-y-5'>
                            <div>
                                <p className='mb-2'>User Name</p>
                                <input type="text"
                                    className='py-2 px-3 outline outline-green-600 w-full rounded-md border-1'
                                    placeholder='Enter your username'
                                    name='full_name'
                                    onChange={(e) => handleChange(e)}
                                    value={inputData.full_name}
                                />
                            </div>

                            <div>
                                <p className='mb-2'>Email</p>
                                <input type="text"
                                    className='py-2 px-3 outline outline-green-600 w-full rounded-md border-1'
                                    placeholder='Enter your Email'
                                    name='email'
                                    onChange={(e) => handleChange(e)}
                                    value={inputData.email}
                                />
                            </div>

                            <div>
                                <p className='mb-2'>Password</p>
                                <input type="text"
                                    className='py-2 px-3 outline outline-green-600 w-full rounded-md border-1'
                                    placeholder='Enter your Password'
                                    name='password'
                                    onChange={(e) => handleChange(e)}
                                    value={inputData.password}
                                />
                            </div>
                            <div>
                                <Button type='submit' sx={{ bgcolor: green[700], padding: ".5rem 0rem" }} className='w-full bg-green-600' variant='contained'> Sign Up</Button>
                            </div>
                        </form>
                        <div className='flex space-x-3 items-center mt-5'>
                            <p>Already Have Account?</p>
                            <Button variant='text' onClick={() => navigate("/signin")}>Signin</Button>

                        </div>
                    </div>

                </div>
                <Snackbar open={openSnackbar} autoHideDuration={6000} onClose={handleSnackbarClose}>
                    <Alert onClose={handleSnackbarClose} severity='success' sx={{ width: '100%' }}>
                        Your Account Successfully Created!
                    </Alert>
                </Snackbar>
            </div>
        </div>
    )
}

export default Signup