import React from 'react';
import Box from '@mui/material/Box';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Logo from './Logo';
import ButtonMenu from './ButtonMenu';

interface Props {
    children: React.ReactNode;
}

export const Layout = (props: Props) => {
    const { children } = props;
    return (
        <Box sx={{ display: 'flex' }}>
            <AppBar position="fixed" sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}>
                <Toolbar>
                    <Logo />
                    <Typography variant="h6" sx={{ flexGrow: 1 }}>
                        CV Manacor
                    </Typography>
                    <ButtonMenu />
                </Toolbar>
            </AppBar>
            <Box
                component="main"
                sx={{
                    p: 2,
                    mt: '64px',
                    flexGrow: 1,
                    minWidth: 0,
                }}
            >
                {children}
            </Box>
        </Box>
    );
};

export default Layout;
