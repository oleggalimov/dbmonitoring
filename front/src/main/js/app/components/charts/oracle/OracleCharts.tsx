import React = require("react");
import { Container, Label } from "reactstrap";
import { CartesianGrid, Line, LineChart, Tooltip, XAxis, YAxis, Legend } from "recharts";
import uuid = require("uuid");

const mapPointsToJSX = (row: Array<any> | null): Array<JSX.Element> | null => {
    if (row == null) {
        return null;
    }
    const result = new Array<JSX.Element>();
    for (const field in row) {
        if (field != "name") {
            result.push(<Line key={uuid.v4()} type="monotone" dataKey={field} stroke={getRandomColor()} />);
        }
    }
    return result;
}

const getRandomColor = () => {
    return `#${(Math.round(Math.random() * 0XFFFFFF)).toString(16)}`;
}

export default (props: any) => {
    const pointsArray = props.points;
    let container: JSX.Element | null = null;
    if (pointsArray == null || pointsArray == undefined) {
        return <div>NO system metrics</div>;
    }

    const lines = mapPointsToJSX(pointsArray[0]); //here enough one element for mapping
    container = <>
        <Container>
            <LineChart width={800} height={600} data={pointsArray} margin={{ top: 5, right: 20, bottom: 5, left: 0 }}>
                <CartesianGrid stroke="#ccc" strokeDasharray="5 5" />
                <XAxis dataKey="name" />
                <YAxis />
                {lines}
                <Tooltip />
            </LineChart>
        </Container>
        <br />
    </>
    return (container);
}