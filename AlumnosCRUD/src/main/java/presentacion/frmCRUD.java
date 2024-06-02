/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package presentacion;

import dtos.AlumnoTablaDTO;
import dtos.EditarAlumnoDTO;
import dtos.GuardarAlumnoDTO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import negocio.IAlumnoNegocio;
import negocio.NegocioException;
import utilerias.JButtonCellEditor;
import utilerias.JButtonRenderer;

/**
 *
 * @author rramirez
 */
public class frmCRUD extends javax.swing.JFrame {

    private int pagina = 1;
    private final int LIMITE = 2;
    private IAlumnoNegocio alumnoNegocio;

    /**
     * Creates new form frmCRUD
     */
    public frmCRUD(IAlumnoNegocio alumnoNegocio) {
        initComponents();
        this.alumnoNegocio = alumnoNegocio;
        this.cargarMetodosIniciales();
    }

    public void cargarMetodosIniciales() {
        //this.cargarConfiguracionInicialPantalla();
        this.cargarConfiguracionInicialTablaAlumnos();
        this.cargarAlumnosEnTabla(pagina, LIMITE);
        BtnGuardar.setEnabled(false);
    }

    public void cargarConfiguracionInicialPantalla() {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void cargarAlumnosEnTabla(int pagina, int limite) {
        try
        {
            List<AlumnoTablaDTO> alumnos = this.alumnoNegocio.buscarAlumnosTabla();
            int total = alumnos.size();
            int inicia = (pagina - 1) * limite;
            int finTotal = Math.min(inicia + limite, total);

            List<AlumnoTablaDTO> paginatedList = alumnos.subList(inicia, finTotal);
            this.llenarTablaAlumnos(paginatedList);

            BtnAtras.setEnabled(pagina > 1);
            BtnSiguiente.setEnabled(finTotal < total);
            LblPagina.setText("Pagina " + pagina);
        } catch (NegocioException ex)
        {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Información", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarConfiguracionInicialTablaAlumnos() {
        ActionListener onEditarClickListener = new ActionListener() {
            final int columnaId = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                //Metodo para editar un alumno
                editar();
            }
        };
        int indiceColumnaEditar = 5;
        TableColumnModel modeloColumnas = this.tblAlumnos.getColumnModel();
        modeloColumnas.getColumn(indiceColumnaEditar)
                .setCellRenderer(new JButtonRenderer("Editar"));
        modeloColumnas.getColumn(indiceColumnaEditar)
                .setCellEditor(new JButtonCellEditor("Editar",
                        onEditarClickListener));

        ActionListener onEliminarClickListener = new ActionListener() {
            final int columnaId = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                //Metodo para eliminar un alumno
                eliminar();
            }
        };
        int indiceColumnaEliminar = 6;
        modeloColumnas = this.tblAlumnos.getColumnModel();
        modeloColumnas.getColumn(indiceColumnaEliminar)
                .setCellRenderer(new JButtonRenderer("Eliminar"));
        modeloColumnas.getColumn(indiceColumnaEliminar)
                .setCellEditor(new JButtonCellEditor("Eliminar",
                        onEliminarClickListener));
    }

    private void llenarTablaAlumnos(List<AlumnoTablaDTO> alumnosLista) {
        DefaultTableModel modeloTabla = (DefaultTableModel) this.tblAlumnos.getModel();

        if (modeloTabla.getRowCount() > 0)
        {
            for (int i = modeloTabla.getRowCount() - 1; i > -1; i--)
            {
                modeloTabla.removeRow(i);
            }
        }

        if (alumnosLista != null)
        {
            alumnosLista.forEach(row ->
            {
                Object[] fila = new Object[5];
                fila[0] = row.getIdAlumno();
                fila[1] = row.getNombres();
                fila[2] = row.getApellidoPaterno();
                fila[3] = row.getApellidoMaterno();
                fila[4] = row.getEstatus();

                modeloTabla.addRow(fila);
            });
        }
    }

    private int getIdSeleccionadoTablaAlumnos() {
        int indiceFilaSeleccionada = this.tblAlumnos.getSelectedRow();
        if (indiceFilaSeleccionada != -1)
        {
            DefaultTableModel modelo = (DefaultTableModel) this.tblAlumnos.getModel();
            int indiceColumnaId = 0;
            int idSocioSeleccionado = (int) modelo.getValueAt(indiceFilaSeleccionada,
                    indiceColumnaId);
            return idSocioSeleccionado;
        } else
        {
            return 0;
        }
    }

    private int idAlumnoEditando = 0;

    private void editar() {
        //Metodo para regresar el alumno seleccionado
        int row = tblAlumnos.getSelectedRow();
        if (row >= 0)
        {
            int idAlumno = (int) tblAlumnos.getValueAt(row, 0);
            try
            {
                GuardarAlumnoDTO alumno = alumnoNegocio.obtenerAlumnoPorId(idAlumno);

                TxtNombre.setText(alumno.getNombres());
                TxtApePaterno.setText(alumno.getApellidoPaterno());
                TxtApeMaterno.setText(alumno.getApellidoMaterno());
                CheckActivo.setSelected("Activo".equalsIgnoreCase(alumno.getEstatus()));

                // Guarda el ID del alumno en un atributo temporal para la edición
                idAlumnoEditando = alumno.getIdAlumno();
                BtnGuardar.setEnabled(true);

            } catch (NegocioException ex)
            {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else
        {
            JOptionPane.showMessageDialog(this, "Seleccione un alumno para editar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        //Metodo para regresar el alumno seleccionado
        int id = this.getIdSeleccionadoTablaAlumnos();
        if (id != 0)
        {
            int confirmacion = JOptionPane.showConfirmDialog(this, "Estas seguro que quieres eliminar este alumno?", "Confirmación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION)
            {
                try
                {
                    alumnoNegocio.eliminarAlumnoPorId(id);
                    cargarAlumnosEnTabla(pagina, LIMITE);
                    JOptionPane.showMessageDialog(this, "Alumno eliminado correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);

                } catch (NegocioException ex)
                {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else
        {
            JOptionPane.showMessageDialog(this, "Seleccione un alumno para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox1 = new javax.swing.JCheckBox();
        LblTitulo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAlumnos = new javax.swing.JTable();
        LblNombre = new javax.swing.JLabel();
        TxtNombre = new javax.swing.JTextField();
        LblApPaterno = new javax.swing.JLabel();
        TxtApePaterno = new javax.swing.JTextField();
        LblApMaterno = new javax.swing.JLabel();
        TxtApeMaterno = new javax.swing.JTextField();
        CheckActivo = new javax.swing.JCheckBox();
        BtnNuevoRegistro = new javax.swing.JButton();
        BtnAtras = new javax.swing.JButton();
        LblPagina = new javax.swing.JLabel();
        BtnSiguiente = new javax.swing.JButton();
        BtnGuardar = new javax.swing.JButton();

        jCheckBox1.setText("jCheckBox1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(645, 410));

        LblTitulo.setText("Administración de alumnos");

        tblAlumnos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nombres", "A Paterno", "A Materno", "Estatus", "Eliminar", "Editar"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAlumnos.setToolTipText("");
        jScrollPane1.setViewportView(tblAlumnos);

        LblNombre.setText("Nombres");

        LblApPaterno.setText("Apellido Paterno");

        TxtApePaterno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtApePaternoActionPerformed(evt);
            }
        });

        LblApMaterno.setText("Apellido Materno");

        TxtApeMaterno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtApeMaternoActionPerformed(evt);
            }
        });

        CheckActivo.setText("Activo");

        BtnNuevoRegistro.setText("Nuevo Registro");
        BtnNuevoRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnNuevoRegistroActionPerformed(evt);
            }
        });

        BtnAtras.setText("Atras");
        BtnAtras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAtrasActionPerformed(evt);
            }
        });

        LblPagina.setText("Pagina 1");

        BtnSiguiente.setText("Siguiente");
        BtnSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSiguienteActionPerformed(evt);
            }
        });

        BtnGuardar.setText("Guardar");
        BtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGuardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(BtnAtras)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LblPagina)
                        .addGap(188, 188, 188)
                        .addComponent(BtnSiguiente))
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(LblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(LblNombre)
                                    .addComponent(TxtNombre))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(TxtApePaterno, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(14, 14, 14)
                                        .addComponent(LblApPaterno)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LblApMaterno)
                                    .addComponent(TxtApeMaterno, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(BtnGuardar, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BtnNuevoRegistro)
                            .addComponent(CheckActivo, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LblNombre)
                    .addComponent(LblApPaterno)
                    .addComponent(LblApMaterno))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TxtApePaterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TxtApeMaterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CheckActivo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnNuevoRegistro)
                    .addComponent(BtnGuardar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnAtras)
                    .addComponent(LblPagina)
                    .addComponent(BtnSiguiente))
                .addGap(29, 29, 29))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TxtApePaternoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtApePaternoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtApePaternoActionPerformed

    private void TxtApeMaternoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtApeMaternoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtApeMaternoActionPerformed

    private void BtnNuevoRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnNuevoRegistroActionPerformed
        // TODO add your handling code here:
        String nombres = TxtNombre.getText();
        String apellidoPaterno = TxtApePaterno.getText();
        String apellidoMaterno = TxtApeMaterno.getText();
        boolean activo = CheckActivo.isSelected();

        if (nombres.isEmpty() || apellidoPaterno.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Ingrese todos los campos obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try
        {
            GuardarAlumnoDTO alumno = new GuardarAlumnoDTO(nombres, apellidoPaterno, apellidoMaterno, activo ? "Activo" : "Inactivo");

            alumnoNegocio.agregarAlumno(alumno);

            cargarAlumnosEnTabla(pagina, LIMITE);

            TxtNombre.setText("");
            TxtApePaterno.setText("");
            TxtApeMaterno.setText("");
            CheckActivo.setSelected(false);

            JOptionPane.showMessageDialog(this, "Registro agregado correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } catch (NegocioException ex)
        {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_BtnNuevoRegistroActionPerformed

    private void BtnAtrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAtrasActionPerformed
        // TODO add your handling code here:
        if (pagina > 1)
        {
            pagina--;
            cargarAlumnosEnTabla(pagina, LIMITE);
        }
    }//GEN-LAST:event_BtnAtrasActionPerformed

    private void BtnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSiguienteActionPerformed
        // TODO add your handling code here:
        pagina++;
        cargarAlumnosEnTabla(pagina, LIMITE);
    }//GEN-LAST:event_BtnSiguienteActionPerformed

    private void BtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGuardarActionPerformed
        // TODO add your handling code here:
        if (idAlumnoEditando != 0)
        {
            try
            {
                String nombres = TxtNombre.getText();
                String apellidoPaterno = TxtApePaterno.getText();
                String apellidoMaterno = TxtApeMaterno.getText();
                String estatus = CheckActivo.isSelected() ? "Activo" : "Inactivo";

                EditarAlumnoDTO alumno = new EditarAlumnoDTO(idAlumnoEditando, nombres, apellidoPaterno, apellidoMaterno, estatus);

                alumnoNegocio.editarAlumno(alumno);

                TxtNombre.setText("");
                TxtApePaterno.setText("");
                TxtApeMaterno.setText("");
                CheckActivo.setSelected(false);
                idAlumnoEditando = 0;
                BtnGuardar.setEnabled(false);

                // Recargar la tabla
                cargarAlumnosEnTabla(pagina, LIMITE);

                JOptionPane.showMessageDialog(this, "Cambios guardados correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } catch (NegocioException ex)
            {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else
        {
            JOptionPane.showMessageDialog(this, "No hay alumno seleccionado para editar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_BtnGuardarActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try
//        {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
//            {
//                if ("Nimbus".equals(info.getName()))
//                {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex)
//        {
//            java.util.logging.Logger.getLogger(frmCRUD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex)
//        {
//            java.util.logging.Logger.getLogger(frmCRUD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex)
//        {
//            java.util.logging.Logger.getLogger(frmCRUD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex)
//        {
//            java.util.logging.Logger.getLogger(frmCRUD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new frmCRUD().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnAtras;
    private javax.swing.JButton BtnGuardar;
    private javax.swing.JButton BtnNuevoRegistro;
    private javax.swing.JButton BtnSiguiente;
    private javax.swing.JCheckBox CheckActivo;
    private javax.swing.JLabel LblApMaterno;
    private javax.swing.JLabel LblApPaterno;
    private javax.swing.JLabel LblNombre;
    private javax.swing.JLabel LblPagina;
    private javax.swing.JLabel LblTitulo;
    private javax.swing.JTextField TxtApeMaterno;
    private javax.swing.JTextField TxtApePaterno;
    private javax.swing.JTextField TxtNombre;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblAlumnos;
    // End of variables declaration//GEN-END:variables
}
