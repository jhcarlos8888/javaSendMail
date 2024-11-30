package com.send;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        // Verifica que el número adecuado de parámetros haya sido ingresado
        if (args.length < 3) {
            System.out.println("Usage: java -jar mail-sender.jar <to> <subject> <body>");
            return;
        }

        String to = args[0];      // Destinatario
        String subject = args[1]; // Asunto
        String body = args[2];    // Cuerpo del mensaje

        // Cargar las propiedades del archivo mail.properties
        Properties props = new Properties();
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("El archivo application.properties no se encuentra en el classpath.");
                return;
            }
            props.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Configuración del servidor SMTP
        final String user = props.getProperty("mail.smtp.username");
        final String password = props.getProperty("mail.smtp.password");


        // Establecer la sesión de correo
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {
            // Crear el mensaje de correo
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            // Enviar el mensaje
            Transport.send(message);

            System.out.println("Correo enviado con éxito!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}