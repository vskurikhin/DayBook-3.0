import './App.css';

import Login from "./component/Login";
import useToken from './hooks/useToken';

import React from 'react';
import {Provider} from 'react-redux';
import {TodoForm} from './component/TodoForm';
import {TodoList} from './component/TodoList';
import {store} from './redux/store';

function App() {
    const {token, setToken} = useToken();

    if (!token) {
        return (
            <Provider store={store}>
                <Login setToken={setToken}/>
            </Provider>
        );
    }

    return (
        <Provider store={store}>
            <div className="App">
                <h1>Список задач</h1>
                <TodoForm/>
                <TodoList/>
            </div>
        </Provider>
    );
}

export default App;
