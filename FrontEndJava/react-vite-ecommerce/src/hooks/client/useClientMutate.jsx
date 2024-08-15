import {useMutation} from "@tanstack/react-query";
import {API_URL} from "../api.jsx";
import axios from "axios";


const postData = async (data) => {

    // eslint-disable-next-line no-useless-catch
    try {
        // console.log(API_URL + '/create', data);
        const response = await axios.post(API_URL + '/userPerson/create', data);
        return response.data;
    } catch (error) {
        // Propaga o erro para o chamador
        throw error;
    }
}

export function useClientMutate() {
    //  console.log('USE CLIENT')
    return useMutation({
        mutationFn: postData
    });
}