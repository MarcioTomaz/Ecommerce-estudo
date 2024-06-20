import React from 'react';

import {Anchor, Button, Checkbox, Group, TextInput} from "@mantine/core";
import {useForm} from "@mantine/form";

const Login = () => {

    const form = useForm({
        mode: 'uncontrolled',
        initialValues: {
            email: '',
            password: ''
        },

        validate: {
            email: (value) => (/^\S+@\S+$/.test(value) ? null : 'Invalid email'),
            password: (value) => (value.length > 3 ? null : 'Invalid password'),
        },
    });

    return (
        <form onSubmit={form.onSubmit((values) => console.log(values))}>
            <TextInput
                withAsterisk
                label="Email"
                placeholder="your@email.com"
                key={form.key('email')}
                {...form.getInputProps('email')}
            />

            <TextInput
                withAsterisk
                label="Password"
                placeholder="password"
                type="password"
                key={form.key('password')}
                {...form.getInputProps('password')}
            />

            <Group justify="flex-end" mt="md">
                <Button type="submit">Submit</Button>
            </Group>

            <Anchor justify="flex" mt="md"
                underline={"hover"}
                href={"/register"}>Cadastrar
            </Anchor>
        </form>
    )
}

export default Login;