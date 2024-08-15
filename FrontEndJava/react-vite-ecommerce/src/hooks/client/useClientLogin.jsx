import {useMutation} from "@tanstack/react-query";
import {API_URL} from "../api.jsx";
import axios from "axios";


const postData = async (data) => {

    // eslint-disable-next-line no-useless-catch
    try {
        //  console.log(API_URL + '/login', data);
        const response = await axios.post(API_URL + '/userPerson/login', data);
        //   console.log('VALOR NO USECLIENTE LOGIN ' + JSON.stringify(response.data))
        return response.data;
    } catch (error) {
        // Propaga o erro para o chamador
        throw error;
    }
}

export function useClientLogin(){
    //  console.log('USE CLIENT LOGIN')
    return useMutation({
        mutationFn: postData
    });
}

