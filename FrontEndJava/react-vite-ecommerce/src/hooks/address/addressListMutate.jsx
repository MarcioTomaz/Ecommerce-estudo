import {useMutation} from "@tanstack/react-query";
import {API_URL} from "../api.jsx";
import axios from "axios";

const postData = async(data) =>{

    // eslint-disable-next-line no-useless-catch
    try {
        const storedUser = localStorage.getItem('userLogin');

        const parsedValue = JSON.parse(storedUser);
        // Acessar o valor do 'id'
        const id = parsedValue.id;

        console.log('STORAGE' + storedUser);

        // console.log(API_URL + '/create_Address', data);
        const response = await axios.get(API_URL + '/address/read/address' + id);
        return response.data;

    }catch(error){
        throw error;
    }
}

export function addressMutate(){
    // console.log('USE ADDRESS LIST')
    return useMutation({
        mutationFn: postData
    });
}