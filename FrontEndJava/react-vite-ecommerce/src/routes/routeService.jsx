import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from "../Pages/Home.jsx";
import PageNotFound from "../components/errorPage/PageNotFound.jsx";
import Login from "../Pages/login/Login.jsx";
import Register from "../Pages/Register/Register.jsx";
import ClientProfile from "../Pages/client/ClientProfile.jsx";
import AddressList from "../Pages/address/AddressList.jsx";
import AddressEdit from "../Pages/address/AddressEdit.jsx";
import {ROUTES} from "./URLS.jsx";
import AddressRegister from "../Pages/address/AddressRegister.jsx";
import CardList from "../Pages/Card/CardList.jsx";
import CardRegister from "../Pages/Card/CardRegister.jsx";

const RouteService = () => {
    return (
        <>
            <BrowserRouter>
                <Routes>
                    <Route path={ROUTES.HOME} element={<Home/>}/>
                    <Route path={ROUTES.HOME_ALT} element={<Home/>}/>

                    <Route path={ROUTES.LOGIN} element={<Login/>}/>
                    <Route path={ROUTES.REGISTER} element={<Register/>}/>

                    <Route path={ROUTES.PROFILE} element={<ClientProfile/>}/>

                    <Route path={ROUTES.ADDRESS_REGISTER} element={<AddressRegister/>}/>
                    <Route path={ROUTES.ADDRESS_LIST} element={<AddressList/>}/>
                    <Route path={ROUTES.ADDRESS_UPDATE} element={<AddressEdit/>}/>

                    <Route path={ROUTES.CARD_LIST} element={<CardList/>} />
                    <Route path={ROUTES.CARD_REGISTER} element={<CardRegister/>} />


                    <Route path="*" element={<PageNotFound />} />
                </Routes>

            </BrowserRouter>
        </>
    )
}

export default RouteService;