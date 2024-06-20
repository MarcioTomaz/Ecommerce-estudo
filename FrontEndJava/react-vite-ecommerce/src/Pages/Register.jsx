import {useForm} from "@mantine/form";
import {Button, Group, Input, TextInput} from "@mantine/core";
import React from "react";
import {IMaskInput} from 'react-imask';
import {useClientMutate} from "../hooks/client/useClientMutate.jsx";
import {DateInput} from "@mantine/dates";


const RegisterPage = () => {

    const form = useForm({
        mode: "uncontrolled",
        initialValues: {
            email: '',
            password: '',
            userType: 'CLIENTE',
            personDTO: (
                {
                    firstName: '',
                    lastName: '',
                    birthDate: null,
                    phoneNumber: null,
                    phoneType: 'FIXO',
                    gender: 'MASCULINO'
                }
            ),
        },
        validate: {
            email: (value) => (/^\S+@\S+$/.test(value) ? null : 'Invalid email'),
            password: (value) => (value.length > 3 ? null : 'Invalid password'),
            personDTO:{
                firstName: (value) =>
                    value.length < 2? 'First name must have at least 2 letters' : null,
                lastName: (value) => (value.length > 1 ? null : 'Invalid lastName'),
                birthDate: (value) => (value != null ? null : 'Invalid birthDate'),
                phoneType: value => (value != null ? null : 'Invalid phone type'),
                phoneNumber: value => (value != null ? null : 'Invalid phone number'),
                gender: (value) => (value != null ? null : 'Invalid gender'),
            }
        }
    })

    const {mutate} = useClientMutate();

    const handleSubmit = (values) => {
        console.log('HANDLE SUBMIT');
        mutate(values);
    };

    return (
        <>
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
                    type="password"
                    key={form.key('password')}
                    {...form.getInputProps('password')}
                />

                <TextInput
                    withAsterisk
                    label="First Name"
                    key={form.key('personDTO.firstName')}
                    {...form.getInputProps('personDTO.firstName')}
                />

                <TextInput
                    withAsterisk
                    label="Last name"
                    key={form.key('personDTO.lastName')}
                    {...form.getInputProps('personDTO.lastName')}
                />

                <DateInput
                    label="birthDate"
                    withAsterisk
                    description="Input description"
                    placeholder="Input placeholder"
                    key={form.key('personDTO.birthDate')}
                    {...form.getInputProps('personDTO.birthDate')}
                />

                <TextInput
                    withAsterisk
                    component={IMaskInput}
                    mask="+55 00 000000000"
                    placeholder="Your phone"
                    label="Your phone number"
                    description="Input description"
                    key={form.key('personDTO.phoneNumber')}
                    {...form.getInputProps('personDTO.phoneNumber')}
                />

                <Group justify="flex-end" mt="md">
                    <Button type="submit">Submit</Button>
                </Group>
            </form>

            <form onReset={form.onReset}></form>
        </>
    )
}
export default RegisterPage;