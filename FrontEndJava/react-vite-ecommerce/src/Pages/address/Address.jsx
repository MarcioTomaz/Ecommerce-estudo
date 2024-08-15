import React from 'react';
import classes from './address.module.css';
import {useNavigate} from "react-router-dom";
import {Button, Container, Group, Paper, TextInput, Title} from "@mantine/core";
import {addressMutate} from "../../hooks/address/addressMutate.jsx";
import {useForm} from "@mantine/form";

const Address = () => {

    const navigate = useNavigate();

    const form = useForm({
        mode: 'uncontrolled',
        initialValues: {
            street: '',
            residencyType: '',
            observation: '',
            number: '',
            district: '',
            zipCode: '',
            logradouro: '',
            city: '',
            country: '',
            state: '',
            addressType: 'ENTREGA'
        },
        validate: {
            street: (value) => (value.trim() ? null : 'Rua não pode ser nula'),
            residencyType: (value) => (value.trim() ? null : 'Tipo de Residência não pode ser nulo'),
            number: (value) => (value.trim() ? null : 'Número não pode ser nulo'),
            district: (value) => (value.trim() ? null : 'Bairro não pode ser nulo'),
            zipCode: (value) => (value.trim() ? null : 'CEP não pode ser nulo'),
            logradouro: (value) => (value.trim() ? null : 'Logradouro não pode ser nulo'),
            city: (value) => (value.trim() ? null : 'Cidade não pode ser nula'),
            country: (value) => (value.trim() ? null : 'País não pode ser nulo'),
            state: (value) => (value.trim() ? null : 'Estado não pode ser nulo'),
            addressType: (value) => (value.trim() ? null : 'Tipo de Endereço não pode ser nulo')
        }
    });

    const {mutate} = addressMutate();

    const handleSubmit = (values) =>{
        // console.log('HANDLE VALUES ' + values);
        mutate(values);
        navigate('/home');
    }

    return (
        <Container size={420} my={40}>
            <Paper withBorder shadow="md" p={30} mt={30} radius="md">
                <Title order={2} align="center" mb="lg">
                    Register
                </Title>


                <form onSubmit={form.onSubmit(handleSubmit)}>
                    <TextInput
                        withAsterisk
                        label="Street"
                        placeholder="street..."
                        {...form.getInputProps('street')}
                        mb="md"
                    />

                    <TextInput
                        withAsterisk
                        label="residencyType"
                        placeholder="residencyType..."
                        {...form.getInputProps('residencyType')}
                        mb="md"
                    />

                    <TextInput
                        withAsterisk
                        label="observation"
                        placeholder="observation..."
                        {...form.getInputProps('observation')}
                        mb="md"
                    />

                    <TextInput
                        withAsterisk
                        label="number"
                        placeholder="number..."
                        {...form.getInputProps('number')}
                        mb="md"
                    />

                    <TextInput
                        withAsterisk
                        label="district"
                        placeholder="district..."
                        {...form.getInputProps('district')}
                        mb="md"
                    />

                    <TextInput
                        withAsterisk
                        label="zipCode"
                        placeholder="zipCode..."
                        {...form.getInputProps('zipCode')}
                        mb="md"
                    />

                    <TextInput
                        withAsterisk
                        label="logradouro"
                        placeholder="logradouro..."
                        {...form.getInputProps('logradouro')}
                        mb="md"
                    />

                    <TextInput
                        withAsterisk
                        label="city"
                        placeholder="city..."
                        {...form.getInputProps('city')}
                        mb="md"
                    />

                    <TextInput
                        withAsterisk
                        label="country"
                        placeholder="country..."
                        {...form.getInputProps('country')}
                        mb="md"
                    />

                    <TextInput
                        withAsterisk
                        label="state"
                        placeholder="state..."
                        {...form.getInputProps('state')}
                        mb="md"
                    />

                    <TextInput
                        withAsterisk
                        label="addressType"
                        placeholder="addressType..."
                        {...form.getInputProps('addressType')}
                        mb="md"
                    />

                    <Group position="right" mt="md">
                        <Button type="submit">Submit</Button>
                    </Group>

                </form>
            </Paper>


        </Container>
    )

}

export default Address;