import React from 'react';
import { useLocation, Link } from "react-router-dom";
import Button from '@mui/material/Button';
import ButtonGroup from '@mui/material/ButtonGroup';

export const ButtonMenu: React.FC = () => {
    const location = useLocation().pathname;
    return (
        <ButtonGroup color="secondary" variant="contained">
            <Button component={Link} to="/horaris" sx={{ opacity: location === '/horaris' ? 1 : 0.7 }}>Horaris</Button>
            <Button component={Link} to="/resultats" sx={{ opacity: location === '/resultats' ? 1 : 0.7 }}>Resultats</Button>
        </ButtonGroup>
    );
};

export default ButtonMenu;
