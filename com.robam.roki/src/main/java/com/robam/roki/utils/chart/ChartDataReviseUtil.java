package com.robam.roki.utils.chart;

import android.util.Log;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Maps;
import com.legent.ui.ext.dialogs.FileDialog;
import com.robam.roki.model.bean.LineChartDataBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 烹饪曲线数据修正类
 */
public class ChartDataReviseUtil {
    public static List<LineChartDataBean> curveDataToLine(String strData) {
        List<LineChartDataBean> list = new ArrayList<>();
        String str[] = strData.split(",");
        for (int i = 0; i < str.length; i++) {
            float xValue = Integer.parseInt(str[i].split(":")[0].trim().replace("\"", ""));
            String strNe[] = str[i].split(":")[1].trim().replace("\"", "").split("-");
            float yValue = (int) Float.parseFloat(strNe[0].trim().replace("\"", ""));
            int power = Integer.parseInt(strNe[1].trim().replace("\"", ""));
            int gear = Integer.parseInt(strNe[2].trim().replace("\"", ""));
            list.add(new LineChartDataBean(xValue, yValue, power, gear));
        }
        Collections.sort(list, (bean1, bean2) -> {
            if (bean1.xValue > bean2.xValue) {
                return 1;
            } else if (bean1.xValue == bean2.xValue) {
                return 0;
            } else {
                return -1;
            }
        });
        return list;
    }
    public static List<LineChartDataBean> steamOvenCurveDataToLine(String strData) {
        List<LineChartDataBean> list = new ArrayList<>();
        String str[] = strData.split(",");
        for (int i = 0; i < str.length; i++) {
            float xValue = Integer.parseInt(str[i].split(":")[0].trim().replace("\"", ""));
            String strNe[] = str[i].split(":")[1].trim().replace("\"", "").split("-");
            float yValue = (int) Float.parseFloat(strNe[0].trim().replace("\"", ""));
            int power = 0;
            int gear = 0;
            list.add(new LineChartDataBean(xValue, yValue, power, gear));
        }
        Collections.sort(list, (bean1, bean2) -> {
            if (bean1.xValue > bean2.xValue) {
                return 1;
            } else if (bean1.xValue == bean2.xValue) {
                return 0;
            } else {
                return -1;
            }
        });
        return list;
    }

    public static List<LineChartDataBean> curveDataToLineFormat(String strData) {
        List<LineChartDataBean> list = new ArrayList<>();
        String str[] = strData.split(",");
        for (int i = 0; i < str.length; i++) {
            float xValue = Integer.parseInt(str[i].split(":")[0].trim().replace("\"", ""));
            String strNe[] = str[i].split(":")[1].trim().replace("\"", "").split("-");
            float yValue = (int) Float.parseFloat(strNe[0].trim().replace("\"", ""));
            int power = Integer.parseInt(strNe[1].trim().replace("\"", ""));
            int gear=0;
            if (strNe.length>2) {
                gear= Integer.parseInt(strNe[2].trim().replace("\"", ""));
            }
            list.add(new LineChartDataBean(xValue, yValue, power, gear));
        }
        Collections.sort(list, (bean1, bean2) -> {
            if (bean1.xValue > bean2.xValue) {
                return 1;
            } else if (bean1.xValue == bean2.xValue) {
                return 0;
            } else {
                return -1;
            }
        });
//        return calculationPoint(list);
        return countPointUnited(list);
    }

    private static List<LineChartDataBean> getPoint(List<LineChartDataBean> dataBeanList) {
        //X轴方向时间长度有效距离
        double xRangeValue = 10;
        //y轴方向有效温度波动范围
        double yWaveValue = 10;
        //拐点位置容器
        LineChartDataBean holderPoint;
        //首尾点默认需要绘制
        dataBeanList.get(0).isBigPoint = true;
        dataBeanList.get(dataBeanList.size() - 1).isBigPoint = true;
        List<LineChartDataBean> pointList = new ArrayList<>();
        pointList.add(dataBeanList.get(0));
        //以第一个数据点位为起点
        holderPoint = dataBeanList.get(0);
        //遍历数据
        for (int i = 0; i < dataBeanList.size() - 1; i++) {
            double xDValue = dataBeanList.get(i).xValue - holderPoint.xValue;
            double yxDValue = dataBeanList.get(i).yValue - holderPoint.yValue;
            //上升趋势数据
            if (dataBeanList.get(i).yValue - holderPoint.yValue >= yWaveValue) {
                //时间间隔够大，并且趋势有变化
                if (dataBeanList.get(i).xValue - holderPoint.xValue >= xRangeValue) {
                    if (dataBeanList.get(i + 1).yValue - dataBeanList.get(i).yValue <= 0) {
                        dataBeanList.get(i).isBigPoint = true;
                        holderPoint = dataBeanList.get(i);
                        pointList.add(dataBeanList.get(i));
                        continue;
                    }
                }
            }
            //平衡趋势数据
            if (dataBeanList.get(i).yValue - holderPoint.yValue < yWaveValue) {
                if (dataBeanList.get(i).xValue - holderPoint.xValue >= xRangeValue) {
                    dataBeanList.get(i).isBigPoint = true;
                    holderPoint = dataBeanList.get(i);
                    pointList.add(dataBeanList.get(i));
                    continue;
                }
            }
            //下降趋势数据蒸箱
            if (holderPoint.yValue - dataBeanList.get(i).yValue >= yWaveValue) {
                if (dataBeanList.get(i).xValue - holderPoint.xValue >= xRangeValue) {
                    if (dataBeanList.get(i).yValue - dataBeanList.get(i + 1).yValue >= 0) {
                        dataBeanList.get(i).isBigPoint = true;
                        holderPoint = dataBeanList.get(i);
                        pointList.add(dataBeanList.get(i));
                        continue;
                    }
                }
            }
        }
        pointList.add(dataBeanList.get(dataBeanList.size() - 1));

//        if (pointList.size()<3)
//            return dataBeanList;
//
//        for (int i = 0; i < pointList.size(); i++) {
//            if (i<3)
//                continue;
//            if (Math.abs(pointList.get(i).yValue - pointList.get(i - 1).yValue) < yWaveValue && Math.abs(pointList.get(i - 1).yValue - pointList.get(i - 2).yValue) < yWaveValue) {
//                pointList.get(i - 1).isBigPoint = false;
//            }
//        }
//        for (int i = 0; i < dataBeanList.size() ; i++) {
//            if(dataBeanList.get(i).isBigPoint){
//                for (int j = 0; j < pointList.size() ; j++) {
//                    if(dataBeanList.get(i).xValue==pointList.get(j).xValue){
//                        dataBeanList.get(i).isBigPoint=pointList.get(j).isBigPoint;
//                    }
//                }
//            }
//        }
        pointHandle(pointList);
        return dataBeanList;
    }

    private static List<LineChartDataBean> countPoint(List<LineChartDataBean> dataBeanList) {
        //X轴方向时间长度有效距离
        int xRangeValue = 30;
        //y轴方向有效温度波动范围
        double yWaveValue = 20;
        //拐点位置容器
        LineChartDataBean holderPoint;
        //首点默认需要绘制
        dataBeanList.get(0).isBigPoint = true;
        dataBeanList.get(dataBeanList.size() - 1).isBigPoint = true;
        //以第一个数据点位为起点
        holderPoint = dataBeanList.get(0);
        //第一次遍历，赋值type 把所有点都描上
        for (int i = 0; i < dataBeanList.size(); i++) {
            if (i == 0) {
                dataBeanList.get(0).trendType = -1;
            } else {
                double yxDValue = dataBeanList.get(i).yValue - dataBeanList.get(i - 1).yValue;
                if (yxDValue > 0) {
                    dataBeanList.get(i).trendType = 1;
                } else if (yxDValue == 0) {
                    dataBeanList.get(i).trendType = 2;
                } else if (yxDValue < 0) {
                    dataBeanList.get(i).trendType = 3;
                }
            }
        }
        //第二次遍历
        for (int i = 1; i < dataBeanList.size() - 1; i++) {
            double xDValue = dataBeanList.get(i).xValue - holderPoint.xValue;
            double yxDValue = dataBeanList.get(i).yValue - holderPoint.yValue;
            //上升断
            if (dataBeanList.get(i).trendType == 1) {
                //后面的点保持上升
                if (dataBeanList.get(i + 1).trendType == 1) {
                    continue;
                }
                //进入平衡段
                if (dataBeanList.get(i + 1).trendType == 2 && xDValue > xRangeValue * 3 && yxDValue > yWaveValue) {//&& yxDValue > yWaveValue
                    dataBeanList.get(i).isBigPoint = true;
                    holderPoint = dataBeanList.get(i);
                    continue;

                }
                //下降
                if (dataBeanList.get(i + 1).trendType == 3 && xDValue > xRangeValue && yxDValue + yWaveValue <= 0) {
                    dataBeanList.get((int) (i - xRangeValue / 2)).isBigPoint = true;
                    if (i + (int) (i - xRangeValue / 2) < dataBeanList.size()) {
                        dataBeanList.get((int) (i - xRangeValue / 2)).isBigPoint = true;
                    }
                    holderPoint = dataBeanList.get((int) (i - xRangeValue / 2));
                    continue;
                }

            }
            //平衡段
            else if (dataBeanList.get(i).trendType == 2) {
                //保持平衡
                if (dataBeanList.get(i + 1).trendType == 2) {
                    continue;
                }
                //进入上升 //下降
                if (dataBeanList.get(i + 1).trendType == 1 || dataBeanList.get(i + 1).trendType == 3) {
                    if (xDValue > xRangeValue && Math.abs(yxDValue) >= yWaveValue) {
                        dataBeanList.get(i).isBigPoint = true;
                        holderPoint = dataBeanList.get(i);
                        continue;
                    }
                }
            }
            //下降段
            else if (dataBeanList.get(i).trendType == 3) {
                //保持下降
                if (dataBeanList.get(i + 1).trendType == 3) {
                    continue;
                }
                //平衡
                //进入平衡段
                if (dataBeanList.get(i + 1).trendType == 2 && xDValue > xRangeValue * 3) {//&& yxDValue > yWaveValue
                    dataBeanList.get(i).isBigPoint = true;
                    holderPoint = dataBeanList.get(i);
                    continue;

                }
                //上升
                if (dataBeanList.get(i + 1).trendType == 1 && xDValue > xRangeValue && yxDValue + yWaveValue >= 0) {
                    dataBeanList.get((int) (i - xRangeValue / 2)).isBigPoint = true;
                    if (i + (int) (i - xRangeValue / 2) < dataBeanList.size()) {
                        dataBeanList.get((int) (i - xRangeValue / 2)).isBigPoint = true;
                    }
                    holderPoint = dataBeanList.get((int) (i - xRangeValue / 2));

                    continue;

                }

            }
        }

        LineChartDataBean holderPoint1 = dataBeanList.get(0);
        for (int i = 1; i < dataBeanList.size() - 1; i++) {
            if (dataBeanList.get(i).isBigPoint) {
                if (holderPoint1.trendType == dataBeanList.get(i).trendType) {
                    dataBeanList.get(i).isBigPoint = false;
                } else {
                    holderPoint1 = dataBeanList.get(i);
                }
            }
        }
        LineChartDataBean holderPoint2 = dataBeanList.get(0);
        for (int i = 1; i < dataBeanList.size() - 1; i++) {
            if (dataBeanList.get(i).isBigPoint) {
                float temp = dataBeanList.get(i).yValue - holderPoint2.yValue;
                if (Math.abs(temp) < yWaveValue * 1.3) {
                    dataBeanList.get(i).yValue = holderPoint2.yValue;
                }
            } else {
                holderPoint2 = dataBeanList.get(i);

            }
        }

        return dataBeanList;
    }

    private static List<LineChartDataBean> countPointUnited(List<LineChartDataBean> dataBeanList) {
        if (dataBeanList == null || dataBeanList.size() == 0) {
            return dataBeanList;
        }
        //X轴方向时间长度有效距离
        int xRangeValue = 30;
        //y轴方向有效温度波动范围
        double yWaveValue = 20;
        //拐点位置容器
        LineChartDataBean holderPoint;
        //首点默认需要绘制
        dataBeanList.get(0).isBigPoint = true;
        dataBeanList.get(dataBeanList.size() - 1).isBigPoint = true;
        //以第一个数据点位为起点
        holderPoint = dataBeanList.get(0);
        //第一次遍历，赋值type 把所有点都描上
        for (int i = 0; i < dataBeanList.size(); i++) {
            dataBeanList.get(i).isBigPoint = true;
            if (i == 0) {
                dataBeanList.get(0).trendType = -1;
            } else {
                double yxDValue = dataBeanList.get(i).yValue - dataBeanList.get(i - 1).yValue;
                if (yxDValue > 0) {
                    dataBeanList.get(i).trendType = 1;
                } else if (yxDValue == 0) {
                    dataBeanList.get(i).trendType = 2;
                } else if (yxDValue < 0) {
                    dataBeanList.get(i).trendType = 3;
                }
            }
        }
        //第二次遍历
        for (int i = 1; i < dataBeanList.size() - 1; i++) {
            double xDValue = dataBeanList.get(i).xValue - holderPoint.xValue;
            double yxDValue = dataBeanList.get(i).yValue - holderPoint.yValue;
            if (Math.abs(xDValue) < xRangeValue || Math.abs(yxDValue) < yWaveValue) {
                dataBeanList.get(i).isBigPoint = false;
                continue;
            }
            holderPoint = dataBeanList.get(i);

        }
        //第三次遍历 把连续上升，下降的点转成平衡点
        LineChartDataBean holderPoint1 = dataBeanList.get(0);
        for (int i = 1; i < dataBeanList.size() - 1; i++) {
            if (dataBeanList.get(i).isBigPoint) {
                if (holderPoint1.trendType == dataBeanList.get(i).trendType) {
                    dataBeanList.get(i).isBigPoint = false;
                    if (dataBeanList.get(i).trendType == 2) {
                        dataBeanList.get(i).yValue = (dataBeanList.get(i).yValue + holderPoint1.yValue) / 2;
                    }
                } else {
                    holderPoint1 = dataBeanList.get(i);
                }
            }
        }
        //第四次遍历 获取峰值可在获取数据入口自行遍历获取isBigPoint为true的点
//        LineChartDataBean holderPoint2 = dataBeanList.get(0);
//        for (int i = 1; i < dataBeanList.size() - 1; i++) {
//            if (dataBeanList.get(i).isBigPoint) {
//                float temp = dataBeanList.get(i).yValue - holderPoint2.yValue;
//                if (Math.abs(temp) < yWaveValue * 1.3) {
//                    dataBeanList.get(i).yValue = holderPoint2.yValue;
//                }
//            } else {
//                holderPoint2 = dataBeanList.get(i);
//
//            }
//        }

        return dataBeanList;
    }

    /**
     * 上升接平衡，
     * 平衡接上升或下降，
     * 下降接平衡，
     * 上升接下降或者下降接平衡的部分取步长中间值拉平
     */
    private static List<LineChartDataBean> calculationPoint(List<LineChartDataBean> dataBeanList) {
        int xMinValid = 10;
        float yMinValid = 10;
        LineChartDataBean holdPoint = dataBeanList.get(0);
        List<LineChartDataBean> pointList = new ArrayList<>();
        for (int i = 0; i < dataBeanList.size(); ) {
            if (i == 0) {
                dataBeanList.get(0).trendType = 1;
            } else {
                double yxDValue = dataBeanList.get(i).yValue - holdPoint.yValue;
                if (yxDValue > yMinValid) {
                    dataBeanList.get(i).trendType = 1;
                } else if (Math.abs(yxDValue) <= yMinValid) {
                    dataBeanList.get(i).trendType = 2;
                } else if (yxDValue + yMinValid < 0) {
                    dataBeanList.get(i).trendType = 3;
                }
            }
            i += xMinValid;
            if (i < dataBeanList.size()) {
                holdPoint = dataBeanList.get(i);

            }
            if (i + xMinValid > dataBeanList.size()) {
                double yxDValue = dataBeanList.get(dataBeanList.size() - 1).yValue - holdPoint.yValue;
                if (yxDValue > yMinValid) {
                    dataBeanList.get(dataBeanList.size() - 1).trendType = 1;
                } else if (Math.abs(yxDValue) <= yMinValid) {
                    dataBeanList.get(dataBeanList.size() - 1).trendType = 2;
                } else if (yxDValue + yMinValid < 0) {
                    dataBeanList.get(dataBeanList.size() - 1).trendType = 3;
                }
            }
        }

        for (int i = 0; i < dataBeanList.size(); i++) {
            if (dataBeanList.get(i).trendType == 1 || dataBeanList.get(i).trendType == 2 || dataBeanList.get(i).trendType == 3) {
                pointList.add(dataBeanList.get(i));
            }
        }
        if (pointList.size() < 3) {
            return null;
        }
        List<LineChartDataBean> packPointList = new ArrayList<>();
        for (int i = pointList.size() - 2; i > 0; i--) {
            if (pointList.get(i).trendType == pointList.get(i + 1).trendType && pointList.get(i).trendType == pointList.get(i - 1).trendType) {
                if (pointList.get(i).trendType == 1 || pointList.get(i).trendType == 3) {
                    if (packPointList.size() > 0) {
                        float vY = 0;
                        for (int j = 0; i < packPointList.size(); j++) {
                            vY += packPointList.get(j).yValue;
                        }
                        pointList.get(i + 1).yValue = vY / packPointList.size();
                        packPointList.clear();
                    }
                } else if (pointList.get(i).trendType == 2) {
                    packPointList.add(pointList.get(i));
                }
                pointList.remove(i);
            }
        }
        for (int i = 0; i < pointList.size(); i++) {
            pointList.get(i).isBigPoint = true;
        }
        return dataBeanList;
    }

    private static List<LineChartDataBean> pointHandle(List<LineChartDataBean> dataBeanList) {
        double k1, k2;//两点间斜率
//        LineChartDataBean holderPoint = dataBeanList.get(0);
        for (int i = 2; i < dataBeanList.size(); i++) {
            k1 = (dataBeanList.get(i).yValue - dataBeanList.get(i - 1).yValue) / (dataBeanList.get(i).xValue - dataBeanList.get(i - 1).xValue);
            k2 = (dataBeanList.get(i - 1).yValue - dataBeanList.get(i - 2).yValue) / (dataBeanList.get(i - 1).xValue - dataBeanList.get(i - 2).xValue);
            double kChangeValue = Math.abs(k2 - k1);
            if (kChangeValue < 0.2) {
                dataBeanList.get(i - 1).isBigPoint = false;
            }

        }
        return dataBeanList;
    }

}
