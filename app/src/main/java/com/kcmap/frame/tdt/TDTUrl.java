package com.kcmap.frame.tdt;

import java.util.Random;


/**
 * @author 作者姓名: 于华
 * @E-mail yuh945@gmail.com
 * @version 创建时间：2012-10-13 下午05:22:27
 * 抓图类
 */
public class TDTUrl  {
    private TianDiTuTiledMapServiceType _tiandituMapServiceType;
    private int _level;
    private int _col;
    private int _row;
    public TDTUrl(int level, int col, int row,TianDiTuTiledMapServiceType tiandituMapServiceType){
        this._level=level;
        this._col=col;
        this._row=row;
        this._tiandituMapServiceType=tiandituMapServiceType;
    }
    public String generatUrl(){
        /**
         * 天地图矢量、影像
         * */
        StringBuilder url=new StringBuilder("http://t");
        Random random=new Random();
        int subdomain = (random.nextInt(6) + 1);
        url.append(subdomain);
        switch(this._tiandituMapServiceType){
            case VEC_C:
                url.append(".tianditu.com/DataServer?T=vec_c&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level);
                break;
            case CVA_C:
                url.append(".tianditu.com/DataServer?T=cva_c&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level);
                break;
            case CIA_C:
                url.append(".tianditu.com/DataServer?T=cia_c&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level);
                break;
            case IMG_C:
                url.append(".tianditu.com/DataServer?T=img_c&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level);
                break;
            case TER_C:
                url.append(".tianditu.com/DataServer?T=ter_c&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level);
                break;
            case CTA_C:
                url.append(".tianditu.com/DataServer?T=cta_c&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level);
                break;
            default:
                return null;
        }
        return url.toString();
    }

}
