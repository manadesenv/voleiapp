import React from 'react';
import Box from '@mui/material/Box';
import logo from "../assets/logo.png";

export const CLUB_ID = 21;

export const Logo: React.FC = () => {
    return <Box
        component="img"
        src={logo}
        alt="Logo"
        sx={{ height: 40, mr: 2 }} />
}

export default Logo;