import {useMutation} from "@tanstack/react-query";
import {API_URL} from "./api.jsx";
import axios from "axios";


const postData = async (data) => {

    console.log(API_URL + '/create', data);
    return await axios.post(API_URL + '/userPerson/create', data);
}

export function useClientMutate(){
    console.log('USE CLIENT')
    return useMutation({
        mutationFn: postData
    });
}