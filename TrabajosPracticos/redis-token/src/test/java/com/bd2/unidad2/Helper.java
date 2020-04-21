package com.bd2.unidad2;

import com.bd2.unidad2.model.Usuario;

public class Helper {

    public static Usuario JUAN_PEREZ(){
        Usuario u = new Usuario();
        u.setApellido("Perez");
        u.setNombre("Juan");
        u.setDni("33.333.333");
        u.setEmail("juan@perez.com");
        u.setPassword("perez");
        return u;
    }

    public static Usuario PITY_MARTINEZ(){
        Usuario u = new Usuario();
        u.setApellido("Martinez");
        u.setNombre("Gonzalo");
        u.setDni("22222222");
        u.setEmail("y_va_el_tercero@river.com");
        u.setPassword("river");
        return u;
    }
}
