package com.huertohogar.huertohogar.Config;

import com.huertohogar.huertohogar.Model.Producto;
import com.huertohogar.huertohogar.Repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader implements CommandLineRunner {

    private final ProductoRepository repo;

    public DataLoader(ProductoRepository repo) {
        this.repo = repo;
    }

    private void agregar(String nombre, int precio, String categoria, String img, String descrpcion,int stock) {
        Producto p = new Producto();
        p.setName(nombre);
        p.setPrecio(precio);
        p.setCategoria(categoria);
        p.setCompania(""); 
        p.setImg(img);
        p.setDescripcion(descrpcion);
        p.setHabilitado(true);
        p.setOferta(false);
        p.setDescuento(0);
        p.setStock(stock); 
        repo.save(p);
    }

    @Override
    public void run(String... args) {
        if (repo.count() > 0) return;

        agregar("Zapallo Italiano", 1200, "verduras", "Img/zapalloitaliano.jpg", "Suave y versátil, perfecto para salteados, rellenos y pastas.", 40);
agregar("Duraznos", 2500, "frutas", "Img/Durazno_1.png", "Duraznos jugosos de temporada.", 35);
agregar("Avena", 2500, "Legumbres-Cereales", "Img/Avena_2.png", "Avena natural, ideal para desayunos saludables.", 60);
agregar("Tomates", 1300, "verduras", "Img/tomate.jpg", "Jugosos y sabrosos, dan color y frescura a tus comidas.", 80);
agregar("Frutillas", 1990, "frutas", "Img/Frutilla_4.png", "Frutillas dulces, ideales para postres.", 50);
agregar("Yogurt", 450, "Lacteos", "Img/Yogurt_3.jpg", "Yogurt natural, fresco y nutritivo.", 70);
agregar("Brócoli", 1690, "verduras", "Img/Brocoli_1.png", "Brócoli fresco y lleno de nutrientes, ideal para ensaladas, sopas y al vapor.", 25);
agregar("Chirimoya", 3800, "frutas", "Img/Chirimoya_1.jpg", "Chirimoya cremosa y muy dulce, conocida como la 'fruta de la paz'.", 30);
agregar("Manjar", 3990, "Lacteos", "Img/Manjar_1.png", "Manjar casero cremoso y suave, elaborado lentamente con leche fresca y azúcar.", 50);
agregar("Arroz", 1900, "Legumbres-Cereales", "Img/Arroz_1.png", "Arroz blanco de grano largo, versátil en la cocina.", 100);
agregar("Naranjas", 1500, "frutas", "Img/Naranja_1.png", "Naranjas jugosas llenas de vitamina C.", 45);
agregar("Mermelada de Mora", 2800, "otros", "Img/Mermelada_mora_1.png", "Mermelada casera de mora, preparada de forma artesanal.", 32);
agregar("Berenjena", 2000, "verduras", "Img/berenjena.png", "Berenjena morada, ideal para asar, freír o rellenar.", 28);
agregar("Higos", 3500, "frutas", "Img/Higo_1.avif", "Higos frescos, dulces y jugosos, ideales para postres o mermeladas.", 22);
agregar("Leche Entera", 1500, "Lacteos", "Img/Leche_3.png", "Leche entera fresca, rica en calcio.", 75);
agregar("Perejil", 700, "verduras", "Img/Perejil_1.webp", "Perejil fresco, perfecto para salsas, guisos y ensaladas.", 50);
agregar("Queso Chanco", 8900, "Lacteos", "Img/quesochanco.png", "Queso chanco tradicional, semimaduro y sabroso.", 20);
agregar("Nueces", 8500, "frutas", "Img/Nuez_1.jpg", "Nueces frescas con alto contenido de omega-3 y energía.", 35);
agregar("Cebolla", 700, "verduras", "Img/Cebolla_2.png", "Cebolla fresca, ideal para todo tipo de platos.", 90);
agregar("Apio", 1100, "verduras", "Img/apio.png", "Crujiente y refrescante, ideal para ensaladas y sopas.", 40);
agregar("Granada", 1990, "frutas", "Img/Granada_1.png", "Granadas con granos rojos brillantes y antioxidantes naturales.", 25);
agregar("Miel Multifloral", 6900, "otros", "Img/Miel_multi_2.jpg", "Miel natural 100% pura y artesanal.", 18);
agregar("Lechuga", 900, "verduras", "Img/Lechuga_2.webp", "Fresca y crujiente, ideal para ensaladas.", 50);
agregar("Plátanos", 1200, "frutas", "Img/Platano_3.png", "Plátanos maduros, energéticos.", 60);
agregar("Alcachofa", 2800, "verduras", "Img/Alcachofa_1.webp", "Alcachofas tiernas, ricas en fibra y antioxidantes.", 25);
agregar("Manzanas Rojas", 990, "frutas", "Img/Manzana_1.png", "Manzanas frescas, crocantes y dulces.", 70);
agregar("Choclo", 1800, "verduras", "Img/Choclo_2.jpg", "Choclo fresco, amarillo y jugoso, perfecto para humitas y pastel de choclo.", 35);
agregar("Mermelada de Frutilla", 2800, "otros", "Img/Mermelada_frutilla_1.png", "Tradicional mermelada de frutilla casera, dulce y natural.", 30);
agregar("Melón Calameño", 3490, "frutas", "Img/MelónCalameño_1.png", "Melón calameño, muy dulce y refrescante en días de calor.", 20);
agregar("Habas", 2000, "Legumbres-Cereales", "Img/Habas_3.png", "Habas frescas y tradicionales en la cocina chilena.", 40);
agregar("Repollo Blanco", 1000, "verduras", "Img/repolloblanco.png", "Repollo blanco, crocante y versátil en ensaladas o guisos.", 50);
agregar("Mermelada de Durazno", 2800, "otros", "Img/Mermelada_durazno_!.png", "Mermelada casera de durazno, hecha con fruta fresca de temporada.", 25);
agregar("Calabaza", 1600, "verduras", "Img/Calabaza-1.png", "Pulpa dulce y cremosa, perfecta para sopas y cremas.", 45);
agregar("Linaza", 2800, "Legumbres-Cereales", "Img/Linaza_1.png", "Semillas de linaza, ricas en fibra y omega-3.", 55);
agregar("Huevos", 3800, "otros", "Img/Huevos.png", "Huevos de gallina frescos, ideales para desayunos y repostería.", 38);
agregar("Pimiento", 1400, "verduras", "Img/Pimenton_1.png", "Dulce y aromático, aporta color y sabor a salteados y ensaladas.", 42);
agregar("Pepinos Dulces", 2990, "frutas", "Img/PepinoDulce_1.png", "Pepinos dulces, refrescantes y suaves, perfectos para ensaladas.", 30);
agregar("Mantequilla", 2900, "Lacteos", "Img/Mantequilla_2.avif", "Mantequilla cremosa, perfecta para untar o cocinar.", 40);
agregar("Castañas", 7600, "frutas", "Img/Castaña_1.jpg", "Castañas frescas, ideales para asar o preparar purés.", 22);
agregar("Cebolla", 700, "verduras", "Img/Cebolla_2.png", "Cebolla fresca, ideal para todo tipo de platos.", 85);
agregar("Repollo Morado", 1100, "verduras", "Img/repollomorado.png", "Repollo morado, de color intenso y sabor fresco.", 30);
agregar("Espárragos", 3500, "verduras", "Img/Espárrago_1.jpg", "Espárragos frescos, tiernos y sabrosos.", 18);
agregar("Lentejas", 2100, "Legumbres-Cereales", "Img/lentejas.jpg", "Lentejas nutritivas y llenas de fibra, perfectas para guisos.", 60);
agregar("Cilantro", 800, "verduras", "Img/Cilantro_2.webp", "Cilantro fresco, aromático e indispensable en la cocina chilena.", 50);
agregar("Ají", 1500, "verduras", "Img/aji.jpg", "Ají fresco, aporta picor y sabor intenso a las comidas.", 28);
agregar("Sandías", 3500, "frutas", "Img/Sandía_1.png", "Sandía fresca, ideal para verano.", 15);
agregar("Piña", 2800, "frutas", "Img/pina.jpg", "Piña tropical, jugosa y refrescante, perfecta para jugos y postres.", 26);
agregar("Mandarinas", 1890, "frutas", "Img/Mandarina_4.png", "Mandarinas dulces y fáciles de pelar, perfectas como snack.", 45);
agregar("Arvejas", 2200, "Legumbres-Cereales", "Img/Arveja_1.jpg", "Arvejas verdes, tiernas y llenas de proteína vegetal.", 60);
agregar("Repollo Blanco", 1000, "verduras", "Img/repolloblanco.png", "Repollo blanco, crocante y versátil en ensaladas o guisos.", 50);
agregar("Espinaca", 1200, "verduras", "Img/Espinaca_1.jpg", "Hojas verdes tiernas, ricas en hierro y vitaminas.", 40);
agregar("Membrillo", 2790, "frutas", "Img/Membrillo_1.png", "Membrillo aromático, ideal para preparar dulces y compotas.", 27);
agregar("Mermelada de Frambuesa", 2800, "otros", "Img/MermeladaFrambuesa_1.webp", "Mermelada de frambuesa fresca, intensa y natural.", 18);
agregar("Tomates Cherry", 1600, "verduras", "Img/tomatecherry_1.png", "Pequeños y dulces, perfectos para snack o ensaladas.", 45);
agregar("Pepino", 900, "verduras", "Img/Pepino_1.png", "Crujiente y refrescante, ideal para ensaladas y aguas saborizadas.", 55);
agregar("Palta Hass", 1600, "verduras", "Img/PaltaHass_4.png", "Textura cremosa y sabor suave, ideal para ensaladas y tostadas.", 22);
agregar("Caqui", 2990, "frutas", "Img/Caqui_2.webp", "Caquis dulces y jugosos, ideales para mermeladas y postres.", 20);
agregar("Kiwis", 1700, "frutas", "Img/Kiwi_3.png", "Kiwi lleno de vitamina C.", 48);
agregar("Coliflor", 1800, "verduras", "Img/Coliflor_2.avif", "Coliflor fresca, ideal para gratinados, sopas y ensaladas.", 38);
agregar("Garbanzos", 2300, "Legumbres-Cereales", "Img/Garbanzo_3.png", "Garbanzos ideales para ensaladas, guisos y hummus.", 70);
agregar("Uvas", 2980, "frutas", "Img/Uva_4.png", "Uvas frescas y jugosas, excelentes para comer al natural o en postres.", 30);
agregar("Queso Mantecoso", 9500, "Lacteos", "Img/QuesoMantecoso_1.webp", "Queso mantecoso, suave y fundente, ideal para sándwiches.", 15);
agregar("Damascos", 2990, "frutas", "Img/damasco_3.png", "Damascos jugosos y dulces, ideales para postres o mermeladas.", 50);
agregar("Zapallo Camote", 1700, "verduras", "Img/Zapallo_2.png", "Sabor dulce y textura suave, ideal para cremas y hornos.", 40);
agregar("Tuna", 2200, "frutas", "Img/Tuna_2.jpg", "Tunas frescas, dulces y refrescantes, típicas de temporada.", 20);
agregar("Ajo", 1200, "verduras", "Img/Ajo_1.jpeg", "Ajo aromático, infaltable en la cocina chilena.", 70);
agregar("Frambuesas", 3500, "frutas", "Img/Frambuesa_3.png", "Frambuesas frescas, pequeñas y llenas de sabor intenso.", 25);
agregar("Mermelada de Higo", 2800, "otros", "Img/MermeladaHigo_1.webp", "Mermelada casera de higo, dulce e intensa.", 18);
agregar("Espinaca", 1200, "verduras", "Img/Espinaca_1.jpg", "Hojas verdes tiernas, ricas en hierro y vitaminas.", 35);
agregar("Mermelada de Arándano", 2800, "otros", "Img/Mermelada_arandano_1.png", "Mermelada artesanal de arándano, llena de sabor.", 20);
agregar("Pomelo", 2300, "frutas", "Img/Pomelo_1.webp", "Pomelos jugosos y ácidos, ricos en vitamina C.", 28);
agregar("Zanahoria", 900, "verduras", "Img/zanahoria.jpg", "Crujiente y naturalmente dulce, rica en betacarotenos.", 90);
agregar("Papas", 1200, "verduras", "Img/papa_3.png", "Versátiles y rendidoras, perfectas para puré, fritas o al horno.", 100);
agregar("Peras", 1100, "frutas", "Img/Pera_2.png", "Peras frescas, textura suave.", 60);
agregar("Almendras", 8900, "frutas", "Img/Almendra_1.jpg", "Almendras naturales, crocantes y llenas de nutrientes.", 30);
agregar("Limón", 1500, "frutas", "Img/Limón_1.avif", "Limones frescos, llenos de jugo ácido y vitamina C.", 70);
agregar("Betarraga", 1600, "verduras", "Img/Beterraga_2.webp", "Betarraga dulce y nutritiva, rica en antioxidantes.", 60);
agregar("Alcayota", 2500, "frutas", "Img/Alcayota_1.webp", "Alcayota fresca, especial para preparar dulces y mermeladas.", 18);
agregar("Arándanos", 4200, "frutas", "Img/Arándano_1.avif", "Arándanos pequeños, azules y llenos de antioxidantes.", 25);
agregar("Melón Tuna", 3200, "frutas", "Img/MelónTuna_1.png", "Melón dulce y refrescante.", 30);
agregar("Queso de Cabra", 10500, "Lacteos", "Img/QuesoCabra_1.png", "Queso de cabra artesanal, de sabor intenso y textura cremosa, ideal para ensaladas y tablas.", 15);
agregar("Mermelada de Alcayota", 2800, "otros", "Img/Mermelada_alca_1.png", "Tradicional mermelada de alcayota, típica de la cocina chilena.", 20);
    }   
}