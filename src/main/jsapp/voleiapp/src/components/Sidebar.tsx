import {
    Drawer,
    List,
    ListItemButton,
    ListItemText,
    Toolbar,
} from "@mui/material";
import { Link } from "react-router-dom";

const drawerWidth = 240;

export default function Sidebar() {
    return (
        <Drawer
            variant="permanent"
            sx={{
                width: drawerWidth,
                "& .MuiDrawer-paper": {
                    width: drawerWidth,
                    boxSizing: "border-box",
                },
            }}
        >
            <Toolbar />
            <List>
                <ListItemButton component={Link} to="/">
                    <ListItemText primary="Home" />
                </ListItemButton>
                <ListItemButton component={Link} to="/partits">
                    <ListItemText primary="Partits" />
                </ListItemButton>
            </List>
        </Drawer>
    );
}