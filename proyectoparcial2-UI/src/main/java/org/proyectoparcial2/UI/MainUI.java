package org.proyectoparcial2.UI;

import javax.swing.*;

import ui.VentanaPrincipal;


public class MainUI 
{
    public static void main( String[] args )
    {
    		SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
