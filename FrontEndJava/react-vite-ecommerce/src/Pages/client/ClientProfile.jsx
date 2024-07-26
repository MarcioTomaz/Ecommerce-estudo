import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {Anchor, Button, Group, TextInput} from "@mantine/core";
import {useForm} from "@mantine/form";

const ClientProfile = () => {

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

    function deleteAccount() {

    }

    return (
        <>
            <section className=" mb-5 my-5">
                <form className="container mt-5 mb-5">
                    <div className="row ">

                        <div className="col-12 ">
                            <div className="form-group">

                                <a href="/home" className="btn btn-outline-secondary mb-2 ">Voltar</a>
                                <a href="/client_update" className="btn btn-primary mb-2 ">Alterar Dados</a>
                                <a href="/password_update" className="btn btn-warning mb-2 ">Alterar Senha</a>

                                <button onClick={deleteAccount} type="button" className="btn btn-danger mb-2 ">Inativar
                                    Conta
                                </button>

                            </div>
                        </div>
                    </div>
                </form>
            </section>
        </>
    )
}

export default ClientProfile;