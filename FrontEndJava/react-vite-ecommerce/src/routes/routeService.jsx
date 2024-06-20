import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from "../Pages/Home.jsx";
import PageNotFound from "../Pages/PageNotFound.jsx";
import Login from "../Pages/Login.jsx";
import Register from "../Pages/Register.jsx";


const RouteService = () => {
    return (
        <>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Home/>}/>
                    <Route path="/login" element={<Login/>}/>
                    <Route path="/register" element={<Register/>}/>
                    <Route path="*" element={<PageNotFound />} />
                </Routes>

            </BrowserRouter>
        </>
    )
}

export default RouteService;