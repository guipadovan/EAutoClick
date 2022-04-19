package com.exitius.eautoclick.utils;

import java.util.HashMap;

/**
 * Criado por Guilherme Padovam em 5/1/2017.
 * Todos os direitos reservados.
 */
public class TimeAPI {

    public static String getExpire(Long time) {
        return getTime(time - System.currentTimeMillis());
    }

    public static String getTime(Long time) {
        if (time == null || time < 0L || time >= 9223372036854775807L)
            return "permanente";

        time = time / 1000L;
        int w = (int) (time / 604800L);
        int d = (int) (time % 604800L / 86400L);
        int h = (int) (time % 86400L / 3600L);
        int m = (int) (time % 3600L / 60L);
        int s = (int) (time % 60L);
        StringBuilder sb = new StringBuilder();
        String sep = ", ";
        if (w > 0) {
            sb.append(w).append(w > 1 ? " semanas" : " semana").append(sep);
        }

        if (d > 0) {
            sb.append(d).append(d > 1 ? " dias" : " dia").append(sep);
        }

        if (h > 0) {
            sb.append(h).append(h > 1 ? " horas" : " hora").append(sep);
        }

        if (m > 0) {
            sb.append(m).append(m > 1 ? " minutos" : " minuto").append(sep);
        }

        if (s > 0) {
            sb.append(s).append(s > 1 ? " segundos" : " segundo").append(sep);
        }

        return sb.substring(0, sb.length() - sep.length());
    }

    public static long toTime(String in) {
        long out = 0L;
        long cur = 0L;
        HashMap<String, Long> multipliers = new HashMap<>();
        String[] curP = "w, week, semana".split(", *");
        int var8 = curP.length;

        int var9;
        String s;
        for (var9 = 0; var9 < var8; ++var9) {
            s = curP[var9];
            multipliers.put(s, 604800L);
        }

        curP = "d, day, dia".split(", *");
        var8 = curP.length;

        for (var9 = 0; var9 < var8; ++var9) {
            s = curP[var9];
            multipliers.put(s, 86400L);
        }

        curP = "h, hour, hora".split(", *");
        var8 = curP.length;

        for (var9 = 0; var9 < var8; ++var9) {
            s = curP[var9];
            multipliers.put(s, 3600L);
        }

        curP = "m, min, minute, minuto".split(", *");
        var8 = curP.length;

        for (var9 = 0; var9 < var8; ++var9) {
            s = curP[var9];
            multipliers.put(s, 60L);
        }

        curP = "s, sec, second, seg, segundo".split(", *");
        var8 = curP.length;

        for (var9 = 0; var9 < var8; ++var9) {
            s = curP[var9];
            multipliers.put(s, 1L);
        }

        StringBuilder var12 = new StringBuilder();
        char[] var13 = in.toCharArray();
        var9 = var13.length;

        for (int var14 = 0; var14 < var9; ++var14) {
            char c = var13[var14];
            if (c > 47 && c < 58) {
                if (var12.length() > 0) {
                    out += cur * to0(multipliers.get(var12.toString()));
                    var12.setLength(0);
                    cur = 0L;
                }

                cur = cur * 10L + (long) (c - 48);
            } else {
                var12.append(c);
            }
        }

        if (var12.length() > 0) {
            out += cur * to0(multipliers.get(var12.toString()));
            cur = 0L;
        }

        return (out + cur) * 1000L;
    }

    private static long to0(Long data) {
        return data == null ? 0L : data;
    }

}
