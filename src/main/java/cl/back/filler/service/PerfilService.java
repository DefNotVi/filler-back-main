package cl.back.filler.service;


import cl.back.filler.model.WaifuHusbando;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PerfilService {
    
    private final List<WaifuHusbando> perfiles = new ArrayList<>();

    public PerfilService() {
        // Quemamos datos de prueba graciosos
        perfiles.add(new WaifuHusbando(1L, "Shrek", "Shrek (El anime)", "Cebollas, pantanos y rock & roll. Busco a mi Fiona o a un burro que no deje de hablar.", "https://via.placeholder.com/300x400?text=Shrek+Guapo", 8));
        perfiles.add(new WaifuHusbando(2L, "Satoru Gojo", "Jujutsu Kaisen", "El más fuerte, pero sobre todo, el que tiene más flow. Si me das un dulce, te paso la tarea.", "https://via.placeholder.com/300x400?text=Gojo", 2));
        perfiles.add(new WaifuHusbando(3L, "Programador Senior C++", "La Vida Real", "Busco a alguien que entienda mis punteros. No me he bañado desde el último commit de producción. Uso Linux de paso.", "https://via.placeholder.com/300x400?text=Senior+CPlusPlus", 10));
        perfiles.add(new WaifuHusbando(4L, "Yugi Muto", "Yu-Gi-Oh!", "Si no crees en el corazón de las cartas, mejor dale a la izquierda. Mi abuelo tiene una tienda de juegos.", "https://via.placeholder.com/300x400?text=Yugi", 4));
    }

    // Devuelve todos los perfiles disponibles para la app
    public List<WaifuHusbando> obtenerTodosLosPerfiles() {
        return perfiles;
    }
}