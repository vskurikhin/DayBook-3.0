import './Login.css';
import {actions} from "../../redux/actions";

import PropTypes from "prop-types";
import {useDispatch} from "react-redux";
import {useNavigate} from 'react-router-dom';
import {useState} from 'react';

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

const Login = (props) => {
    const {setUser, setToken} = props;

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const navigate = useNavigate();

    // создаем две функции для отправки экшенов в хранилище
    const dispatch = useDispatch();
    const login = () => dispatch(actions.user.login(username));

    const handleSubmit = async e => {
        e.preventDefault()
        if (!username || !password) return;
        const token = await loginUser({
            username,
            password
        }, login)
        setUser({name: username, token: token});
        setToken(token)
        navigate('/dashboard');
    }

    return (
        <section className='section'>
            <form className='form' onSubmit={handleSubmit}>
                <h5>login</h5>
                <div className='form-row'>
                    <label htmlFor='name' className='form-label'>
                        name
                    </label>
                    <input
                        type='text'
                        className='form-input'
                        id='name'
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </div>
                <div className='form-row'>
                    <label htmlFor='password' className='form-label'>
                        password
                    </label>
                    <input
                        type='password'
                        className='form-input'
                        id='password'
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                <button type='submit' className='btn btn-block'>
                    login
                </button>
            </form>
        </section>
    );
};


Login.propTypes = {
    setUser: PropTypes.func.isRequired,
    setToken: PropTypes.func.isRequired
}

export default Login;
