import { createContext, useState, useEffect } from "react";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [userToken, setUserToken] = useState(() => {
        return localStorage.getItem("userLogin") || null;
    });

    const login = (token) => {
        setUserToken(token);
        localStorage.setItem("userLogin", token);
    };

    const logout = () => {
        setUserToken(null);
        localStorage.removeItem("userLogin");
    };

    return (
        <AuthContext.Provider value={{ userToken, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};
