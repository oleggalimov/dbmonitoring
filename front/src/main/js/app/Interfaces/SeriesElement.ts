export interface SeriesElement {
    readonly name:string;
    readonly tags:Tags,
    readonly columns:Array<string>;
    readonly values:Array<Array<string>>;
}

interface Tags{
    readonly metric_name:string;
}