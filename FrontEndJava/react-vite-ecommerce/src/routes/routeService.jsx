import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from "../Pages/Home.jsx";
import PageNotFound from "../components/errorPage/PageNotFound.jsx";
import Login from "../Pages/login/Login.jsx";
import Register from "../Pages/Register/Register.jsx";
import ClientProfile from "../Pages/client/ClientProfile.jsx";
import AddressList from "../Pages/address/AddressList.jsx";
import AddressEdit from "../Pages/address/AddressEdit.jsx";

const RouteService = () => {
    return (
        <>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Home/>}/>
                    <Route path="/home" element={<Home/>}/>
                    <Route path="/login" element={<Login/>}/>
                    <Route path="/register" element={<Register/>}/>
                    <Route path="/profile" element={<ClientProfile/>}/>
                    <Route path="/address/update/:id" element={<AddressEdit/>}/>
                    <Route path="/address/list" element={<AddressList/>}/>

                    <Route path="*" element={<PageNotFound />} />
                </Routes>

            </BrowserRouter>
        </>
    )
}

export default RouteService;