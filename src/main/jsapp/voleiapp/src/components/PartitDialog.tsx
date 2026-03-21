import React from 'react';
import Box from '@mui/material/Box';
import Stack from '@mui/material/Stack';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import { localDate } from '../utils';

export type PartitDialogProps = {
    open: boolean;
    title?: string;
    partit: any;
    fullScreen?: boolean;
    onClose: () => void;
};

export const PartitDialog: React.FC<PartitDialogProps> = (props) => {
    const { open, title, partit, fullScreen, onClose } = props;
    return <Dialog fullScreen={fullScreen} open={open} onClose={onClose} fullWidth maxWidth="lg">
        {title && <DialogTitle>{title}</DialogTitle>}
        {partit && <DialogContent>
            <Stack spacing={2}>
                <Box sx={{ textAlign: 'center' }}>
                    <Typography>{partit.campNom}</Typography>
                    <Typography>{partit.municipi}</Typography>
                    {partit.resultat && <Typography>{localDate(partit.data)}</Typography>}
                </Box>
                <Grid container spacing={2} sx={{ display: 'flex' }}>
                    <Grid size={5} sx={{ textAlign: 'center' }}>
                        <img src={'/api/logo?clubId=' + partit.clubLocalId} width="120" />
                        <Typography variant="h6">{partit.equipLocal}</Typography>
                    </Grid>
                    <Grid size={2} sx={{ height: 'auto', display: 'flex', alignItems: 'center', justifyContent: 'center', textAlign: 'center' }}>
                        {partit.resultat ? <Typography variant="h4">{partit.resultat}</Typography> : <Typography variant="h6">{localDate(partit.data)}</Typography>}
                    </Grid>
                    <Grid size={5} sx={{ textAlign: 'center' }}>
                        <img src={'/api/logo?clubId=' + partit.clubVisitantId} width="120" />
                        <Typography variant="h6">{partit.equipVisitant}</Typography>
                    </Grid>
                </Grid>
            </Stack>
        </DialogContent>}
        <DialogActions>
            <Button onClick={onClose}>Tancar</Button>
        </DialogActions>
    </Dialog>;
};

export default PartitDialog;