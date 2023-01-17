import About from './pages/About';
import Dashboard from './pages/Dashboard';
import Error from './pages/Error';
import Home from './pages/Home';
import Login from './components/Login/Login';
import Products from './pages/Products';
import ProtectedRoute from './pages/ProtectedRoute';
import SharedLayout from './pages/SharedLayout';
import SharedProductLayout from './pages/SharedProductLayout';
import SingleProduct from './pages/SingleProduct';
import useToken from "./hooks/useToken";
import {store} from './redux/store';

import {BrowserRouter, Routes, Route} from 'react-router-dom';
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
                        <Route index element={<Home/>}/>
                        <Route path='about' element={<About/>}/>

                        <Route path='products' element={<SharedProductLayout/>}>
                            <Route index element={<Products/>}/>
                            <Route path=':productId' element={<SingleProduct/>}/>
                        </Route>

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
