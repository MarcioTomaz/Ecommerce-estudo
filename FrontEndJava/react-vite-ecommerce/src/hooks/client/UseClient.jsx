import {useQuery, useQueryClient} from "@tanstack/react-query";
import axios from "axios";
import {API_URL} from "./api.jsx";


const fetchData = async () => {
    const response = await axios.get(API_URL + '/userPerson');

    return response?.data?.data;
}

export function useClient() {

    const query = useQuery({
        queryFn : fetchData(),
        queryKey : ['client-data']
    });

    return query;
}