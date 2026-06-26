package cl.back.filler.config;

import cl.back.filler.model.WaifuHusbando;
import cl.back.filler.repository.PerfilRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PerfilRepository perfilRepository;

    public DataInitializer(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Al encenderse la app, si la DB está vacía, mete los datos
        if (perfilRepository.count() == 0) {
           perfilRepository.save(new WaifuHusbando(null, "Shrek", "Shrek (El anime)", "A donde está mi Fiona para soplarle el sapo.", "https://i.imgur.com/dJi55Or.jpeg", 8));
            perfilRepository.save(new WaifuHusbando(null, "Satoru Gojo", "Jujutsu Kaisen", "El más fuerte. Si me das un dulce, te paso la tarea.", "https://i.imgur.com/8jV8WXd.jpeg", 2));
            perfilRepository.save(new WaifuHusbando(null, "Programador C++", "La Vida Real", "Uso Linux. No me he bañado desde el último commit a producción.", "/cpp.jpg", 10));
            perfilRepository.save(new WaifuHusbando(null, "Yugi Muto", "Yu-Gi-Oh!", "Si no crees en el corazón de las cartas, dale a la izquierda.", "https://i.imgur.com/Gc4NwaP.jpeg", 4));
            perfilRepository.save(new WaifuHusbando(null, "Buzz Lightyear", "Toy Story", "Hasta el pisco y mas allá.", "https://i.imgur.com/M70VCww.png", 4));
            perfilRepository.save(new WaifuHusbando(null, "Star Platinum", "Jojo's Bizarre Adventure: Stardust Crusaders", "ora, ora ora, ora.", "https://i.imgur.com/pm0m6gd.png", 3));
            perfilRepository.save(new WaifuHusbando(null, "El genio", "Aladin", "Cumplo tus deseos en la cama.", "https://i.imgur.com/1JS2bBS.png", 2));
            perfilRepository.save(new WaifuHusbando(null, "Gayrio y gaygi", "Super Homo bros", "Te vamos a examinar la tubería.", "https://i.imgur.com/SkcJQqU.png", 4));
            perfilRepository.save(new WaifuHusbando(null, "Iggy", "Yu-Gi-Oh!", "Te voy a morder el chicle de café.", "https://i.imgur.com/DLroZZM.png", 2));
            perfilRepository.save(new WaifuHusbando(null, "Franky", "One piece", "No sé no me vi one piece sdkjfhlskjdfhljkdf", "https://i.imgur.com/VAdKzeg.png", 2));
            perfilRepository.save(new WaifuHusbando(null, "Joseph Joestar", "Jojo's bizarre adventure: Batlle Tendency", "La siguiente palabra que dirás es: Sal conmigo.", "https://i.imgur.com/beWKYRi.jpeg", 2));




            System.out.println("¡Base de datos H2 inicializada con perfiles geek con éxito!");
        }
    }
}