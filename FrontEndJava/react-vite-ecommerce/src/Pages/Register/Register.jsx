import React from "react";
import {Button, Group, TextInput, Paper, Container, Title} from "@mantine/core";
import {IMaskInput} from 'react-imask';
import {useClientMutate} from "../../hooks/client/useClientMutate.jsx";
import {DateInput} from "@mantine/dates";
import {useNavigate} from 'react-router-dom';
import {useForm} from "@mantine/form";

const RegisterPage = () => {
    const navigate = useNavigate();

    const form = useForm({
        mode: "uncontrolled",
        initialValues: {
            email: '',
            password: '',
            userType: 'CLIENTE',
            personDTO: {
                firstName: '',
                lastName: '',
                birthDate: null,
                phoneNumber: null,
                phoneType: 'FIXO',
                gender: 'MASCULINO'
            },
        },
        validate: {
            email: (value) => (/^\S+@\S+$/.test(value) ? null : 'Invalid email'),
            password: (value) => (value.length > 3 ? null : 'Invalid password'),
            personDTO: {
                firstName: (value) =>
                    value.length < 2 ? 'First name must have at least 2 letters' : null,
                lastName: (value) => (value.length > 1 ? null : 'Invalid lastName'),
                birthDate: (value) => (value != null ? null : 'Invalid birthDate'),
                phoneType: value => (value != null ? null : 'Invalid phone type'),
                phoneNumber: value => (value != null ? null : 'Invalid phone number'),
                gender: (value) => (value != null ? null : 'Invalid gender'),
            }
        }
    });

    const {mutate} = useClientMutate();

    const handleSubmit = (values) => {
        // console.log('HANDLE SUBMIT');
        mutate(values);
        navigate('/home');
    };

    return (
        <Container size={420} my={40}>
            <Paper withBorder shadow="md" p={30} mt={30} radius="md">
                <Title order={2} align="center" mb="lg">
                    Register
                </Title>


                    <form onSubmit={form.onSubmit(handleSubmit)}>
                        <TextInput
                            withAsterisk
                            label="Email"
                            placeholder="your@email.com"
                            {...form.getInputProps('email')}
                            mb="md"
                        />

                        <TextInput
                            withAsterisk
                            label="Password"
                            type="password"
                            {...form.getInputProps('password')}
                            mb="md"
                        />

                        <TextInput
                            withAsterisk
                            label="First Name"
                            {...form.getInputProps('personDTO.firstName')}
                            mb="md"
                        />

                        <TextInput
                            withAsterisk
                            label="Last Name"
                            {...form.getInputProps('personDTO.lastName')}
                            mb="md"
                        />

                        <DateInput
                            label="Birth Date"
                            withAsterisk
                            {...form.getInputProps('personDTO.birthDate')}
                            mb="md"
                        />

                        <TextInput
                            withAsterisk
                            component={IMaskInput}
                            mask="+55 00 000000000"
                            placeholder="Your phone number"
                            label="Phone Number"
                            {...form.getInputProps('personDTO.phoneNumber')}
                            mb="md"
                        />

                        <Group position="right" mt="md">
                            <Button type="submit">Submit</Button>
                        </Group>
                    </form>
            </Paper>
        </Container>
    );
};

export default RegisterPage;
