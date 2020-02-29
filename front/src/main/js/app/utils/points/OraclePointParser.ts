import { PointParser } from "../../Interfaces/PointsParser";
import { SeriesElement } from "../../Interfaces/SeriesElement";
import MsgCard from "../../components/common/MsgCard";

export class OraclePointParser implements PointParser {
    series?: any;

    constructor(series: any) {
        this.series = series;
    }

    parse(): Array<any> | null {
        if (this.series == null) {
            return null;
        }
        const seriesArray: Array<SeriesElement> = this.series.series;
        try {
            if (seriesArray.length > 0) {
                const points = new Map<number, any>();
                seriesArray.forEach(element => {
                    const metricName = element.tags.metric_name;
                    element.values.forEach(value => {
                        const rawDate = new Date(value[0]);
                        rawDate.setSeconds(0);
                        const key = rawDate.getTime();
                        const record = points.get(key);
                        if (record == undefined) {
                            const newObject = {};
                            newObject["name"] = `${rawDate.toLocaleDateString()} ${rawDate.toLocaleTimeString()}`;
                            newObject[metricName] = value[1];
                            points.set(key, newObject);
                        } else {
                            record[metricName] = value[1];
                        }
                    }
                    );
    
                });
                const resultArray = Array.from(points.values());
                return resultArray;
            } else {
                return null;
            }
        } catch (ex) {
            console.debug(`Error with parsing oracle metrics`);
            return null
        }
    }
}