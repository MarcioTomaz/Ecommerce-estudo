import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {Anchor, Text, Button, Container, Group, TextInput, Title, Paper} from "@mantine/core";
import {useForm} from "@mantine/form";
import {useClientLogin} from "../../hooks/client/useClientLogin.jsx";
import classes from './login.module.css';

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

    // eslint-disable-next-line no-unused-vars
    const [errorMessage, setErrorMessage] = useState(null);
    const {mutate, isError, error} = useClientLogin();
    const navigate = useNavigate();

    const handleSubmit = (values) => {
        localStorage.removeItem('userLogin');

        mutate(values, {
            onSuccess: (data) => {
                localStorage.setItem('userLogin', JSON.stringify(data));

                navigate('/profile');
            },
            onError: (error) => {
                localStorage.removeItem('userLogin');
                console.error('Error during mutation:', error);
                setErrorMessage(error.response?.data?.message || 'Ocorreu um erro durante a requisição.');
            }
        });
    };

    return (

        <>
            <Container size={420} my={40}>
                <Title ta="center" className={classes.title}>
                    Welcome back!
                </Title>

                <Text c="dimmed" size="sm" ta="center" mt={5}>
                    Do not have an account yet?{' '}
                    <Anchor size="sm" href='/register'>
                        Create account
                    </Anchor>
                </Text>

                <Paper withBorder shadow="md" p={30} mt={30} radius="md">
                    <form onSubmit={form.onSubmit(handleSubmit)}>
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

                        <Anchor component="button" size="sm" >
                            Forgot password?
                        </Anchor>
                    </form>
                </Paper>

                <div>
                    {errorMessage && <div className="error-message">{errorMessage}</div>}
                    {/* Formulário e outros componentes */}
                </div>
            </Container>

        </>
    )
}

export default Login;