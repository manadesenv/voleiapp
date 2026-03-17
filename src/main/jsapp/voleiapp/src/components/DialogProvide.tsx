import React from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button,
} from "@mui/material";

export type ConfirmOptions = {
    title: string;
    description?: string;
};

export type AlertOptions = {
    title: string;
    description?: string;
};

export type DialogType = "confirm" | "alert";

export type DialogState = {
    type: DialogType;
    title: string;
    description?: string;
};

export interface DialogContextValue {
    confirm: (options: ConfirmOptions) => Promise<boolean>;
    alert: (options: AlertOptions) => Promise<void>;
}

export const DialogContext = React.createContext<DialogContextValue | null>(null);

export function useDialog(): DialogContextValue {
    const ctx = React.useContext(DialogContext);
    if (!ctx) {
        throw new Error("useDialog must be used inside DialogProvider");
    }
    return ctx;
}

type Resolver = (value: any) => void;

export function DialogProvider({ children }: { children: React.ReactNode }) {
    const [dialog, setDialog] = React.useState<DialogState | null>(null);
    const resolver = React.useRef<Resolver | null>(null);
    const openDialog = (state: DialogState) => {
        setDialog(state);
        return new Promise((resolve) => {
            resolver.current = resolve;
        });
    };
    const closeDialog = (result: any) => {
        setDialog(null);
        resolver.current?.(result);
    };
    const confirm = async (options: ConfirmOptions): Promise<boolean> => {
        const result = await openDialog({
            type: "confirm",
            ...options,
        });
        return Boolean(result);
    };
    const alert = async (options: AlertOptions): Promise<void> => {
        await openDialog({
            type: "alert",
            ...options,
        });
    };
    return (
        <DialogContext.Provider value={{ confirm, alert }}>
            {children}
            <Dialog open={Boolean(dialog)} onClose={() => closeDialog(false)}>
                <DialogTitle>{dialog?.title}</DialogTitle>
                <DialogContent>{dialog?.description}</DialogContent>
                <DialogActions>
                    {dialog?.type === "confirm" && (
                        <Button onClick={() => closeDialog(false)}>Cancel</Button>
                    )}
                    <Button
                        variant="contained"
                        onClick={() => closeDialog(true)}
                    >
                        Ok
                    </Button>
                </DialogActions>
            </Dialog>
        </DialogContext.Provider>
    );
}