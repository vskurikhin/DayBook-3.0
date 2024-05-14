import About from './pages/About';
import Dashboard from './pages/Dashboard';
import Error from './pages/Error';
import Login from './components/Login/Login';
import Posts from "./components/Posts/Posts";
import ProtectedRoute from './pages/ProtectedRoute';
import SharedLayout from './pages/SharedLayout';
import SharedPostsLayout from './components/Posts/SharedPostsLayout';
import SinglePost from './components/Posts/SinglePost';
import useToken from "./hooks/useToken";
import {store} from './redux/store';

import {BrowserRouter, Route, Routes} from 'react-router-dom';
import {Provider} from 'react-redux';
import {useState} from 'react';

function App() {
    const [user, setUser] = useState(null);
    const {token, setToken} = useToken();

    return (
        <Provider store={store}>
            <BrowserRouter>
                <Routes>
                    <Route path='/' element={<SharedLayout/>}>
                        <Route path='/' element={<SharedPostsLayout/>}>
                            <Route index element={<Posts/>}/>
                            {/*<Route path=':productId' element={<SinglePost/>}/>*/}
                        </Route>
                        <Route path='about' element={<About/>}/>

                        <Route path='login' element={<Login setToken={setToken} setUser={setUser}/>}/>
                        <Route
                            path='dashboard'
                            element={
                                <ProtectedRoute user={user}>
                                    <Dashboard user={user}/>
                                </ProtectedRoute>
                            }
                        />
                        <Route path='*' element={<Error/>}/>
                    </Route>
                </Routes>
            </BrowserRouter>
        </Provider>
    );
}

export default App;
