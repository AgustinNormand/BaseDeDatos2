package com.bd2.unidad2.repository.utils;

/**
 * Purpose: Genera un token random
 * <p>
 * <br>
 *
 * @author : lucasmufato
 **/
public class TokenGenerator {

  public static String generate(){
    int tokenSize = 60;
    String caracteresValidos= "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-|_?=&%$#!*^";
    StringBuilder tokenBuilder = new StringBuilder();
    for(int i=0;i<tokenSize;i++){
      int posicionCaracter = (int) ( Math.random() * caracteresValidos.length() );
      tokenBuilder.append(caracteresValidos.charAt(posicionCaracter));
    }
    return tokenBuilder.toString();
  }

}
