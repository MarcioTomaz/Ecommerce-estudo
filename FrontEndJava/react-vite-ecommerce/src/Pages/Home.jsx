import React from 'react';

import {Anchor, Button} from "@mantine/core";

const Home = () => {

    return (
        <>
            <h1>Home Page</h1>
            <Button color={"red"} radius={"xl"}>Button</Button>
            <br/>
            <br/>


            <Anchor
                underline={"hover"}
                href={"/login"}>login
            </Anchor>
        </>

    )
}

export default Home;