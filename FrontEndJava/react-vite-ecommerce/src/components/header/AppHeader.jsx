import React from 'react';
import {Group, Autocomplete, rem, Anchor} from '@mantine/core';
import { IconSearch } from '@tabler/icons-react';
import classes from './HeaderSearch.module.css';
import {MantineLogo} from "@mantinex/mantine-logo";

const links = [
    { link: '/home', label: 'Home' },
    { link: '/products', label: 'Produtos' },
    { link: '/learn', label: 'Sobre' },
    { link: '/login', label: 'Login' },
];

const items = links.map((link) => (
    <Anchor
        key={link.label}
        href={link.link}
        className={classes.link}>
        {link.label}
    </Anchor>
));

function AppHeader() {

    return (
        <>
            <header className={classes.header}>
                <div className={classes.inner}>
                    <Group>
                        <MantineLogo size={28} />
                    </Group>

                    <Group ml={50} gap={5} className={classes.links} visibleFrom="sm">
                        {items}
                    </Group>
                    <Autocomplete
                        className={classes.search}
                        placeholder="Search"
                        leftSection={<IconSearch style={{width: rem(16), height: rem(16)}} stroke={1.5}/>}
                        data={['React', 'Angular', 'Vue', 'Next.js', 'Riot.js', 'Svelte', 'Blitz.js']}
                        visibleFrom="xs"/>
                </div>
            </header>

        </>

    );
}

export default AppHeader;
