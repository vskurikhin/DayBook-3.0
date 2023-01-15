import './Login.css'
import {actions} from '../redux/actions';
import {selectors} from '../redux/selectors';

import PropTypes from 'prop-types'
import React, {useState} from 'react'
import {useDispatch, useSelector} from 'react-redux';

async function loginUser(credentials, login) {
    return fetch('/api/v1/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(credentials)
    }).then(resp => resp.json())
        .then(data => {
            if (data.code && data.code === 202 && data.payload) {
                login()
                return data.payload
            } else {
                return null
            }
        })
}

export default function Login(props) {

    const {setToken} = props;
    const [username, setUserName] = useState();
    const [password, setPassword] = useState();

    // извлекаем их хранилища данные по авторизации (да,нет)
    const auth = useSelector(selectors.user.auth);

    // создаем две функции для отправки экшенов в хранилище
    const dispatch = useDispatch();
    const login = () => dispatch(actions.user.login(username));
    const logout = () => dispatch(actions.user.logout());


    const handleSubmit = async e => {
        e.preventDefault()
        const token = await loginUser({
            username,
            password
        }, login)
        setToken(token)
    }

    return (
        <div className="login-wrapper">
            <h1>Please Log In</h1>
            <form onSubmit={handleSubmit}>
                <label>
                    <p>Username</p>
                    <input type="text" onChange={e => setUserName(e.target.value)}/>
                </label>
                <label>
                    <p>Password</p>
                    <input type="password" onChange={e => setPassword(e.target.value)}/>
                </label>
                <div>
                    <button type="submit">Submit</button>
                </div>
            </form>
        </div>
    )
}

Login.propTypes = {
    setToken: PropTypes.func.isRequired
}
