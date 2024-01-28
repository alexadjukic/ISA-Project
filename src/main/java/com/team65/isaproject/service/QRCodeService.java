package com.team65.isaproject.service;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.team65.isaproject.model.appointment.Appointment;
import com.team65.isaproject.model.equipment.Equipment;
import com.team65.isaproject.repository.CompanyRepository;
import com.team65.isaproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import com.google.zxing.NotFoundException;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QRCodeService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final EquipmentService equipmentService;

    public BufferedImage generateQRCode(String data) throws WriterException {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    public String generateQRCodeData(Appointment appointment) {
        var receiver = userRepository.findById(appointment.getUserId()).orElseThrow().getFirstName();
        var administrator = userRepository.findById(appointment.getAdminId()).orElseThrow().getFirstName() +
                userRepository.findById(appointment.getAdminId()).orElseThrow().getLastName();
        var company = companyRepository.findById(appointment.getCompanyId()).orElseThrow().getCompanyName();
        var equipment = equipmentService.findAllByAppointmentId(appointment.getId());
        StringBuilder equipmentNames = new StringBuilder();
        if (equipment.isPresent()) {
            for (Equipment item :
                    equipment.get()) {
                equipmentNames.append(item.getName()).append(", ");
            }
            if (!equipmentNames.isEmpty()) {
                equipmentNames.setLength(equipmentNames.length() - 2);
            }
        }

        return String.format("""
                        {
                            Date & time: %s,
                            equipment: [ %s ],
                            receiver: %s,
                            administrator: %s,
                            company: %s,
                            appointmentId: %d
                         }""",
                appointment.getDateTime().toString(),
                equipmentNames,
                receiver,
                administrator,
                company,
                appointment.getId());
    }

    public String decodeQRCode(MultipartFile qrCodeImage) throws IOException, NotFoundException {
        try {
            // Check if the file is an image
            if (!qrCodeImage.getContentType().startsWith("image/")) {
                throw new IllegalArgumentException("Uploaded file is not an image.");
            }

            // Obtain an InputStream from the MultipartFile
            try (InputStream inputStream = qrCodeImage.getInputStream()) {
                BufferedImage bufferedImage = ImageIO.read(inputStream);
                LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                Result result = new MultiFormatReader().decode(bitmap);
                return result.getText();
            }
        } catch (IOException | NotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Error decoding QR code", e);
        }
    }
}
