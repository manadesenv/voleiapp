import React from 'react';
import Box from '@mui/material/Box';

interface Props {
    children: React.ReactNode;
}

export const Layout = (props: Props) => {
    const { children } = props;
    return (
        <Box sx={{ display: "flex" }}>
            {/*<AppBar
                position="fixed"
                sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}
            >
                <Toolbar>
                    <Typography variant="h6">CV Manacor</Typography>
                </Toolbar>
            </AppBar>
            <Sidebar />*/}
            <Box
                component="main"
                sx={{
                    flexGrow: 1,
                    p: 3,
                }}
            >
                {/*<Toolbar />*/}
                {children}
            </Box>
        </Box>
    );
}

export default Layout;