import {useState} from 'react';

const TOKEN = 'token';

export default function useToken() {
    const getToken = () => {
        return sessionStorage.getItem(TOKEN);
    };
    const [token, setToken] = useState(getToken());
    const saveToken = userToken => {
        if (userToken) {
            sessionStorage.setItem(TOKEN, JSON.stringify(userToken));
        } else {
            sessionStorage.removeItem(TOKEN)
        }
        setToken(userToken);
    };
    return {
        setToken: saveToken,
        token
    }
}
