import java.util.ArrayList;

public class Graph {
    ArrayList<Coord> points;
    ArrayList<Edge> edges;

    public Graph(ArrayList<Coord> listPiece){
        this.points = listPiece;
        edges = new ArrayList<>();
        generateMinEdgeOfAll();
    }

    public void addPoint(Coord coord){
        if(!isThereCoord(coord)) {
            points.add(coord);
        }
    }

    public void addEdge(Coord pointA, Coord pointB, int value){
        addEdge(new Edge(pointA, pointB, value));
    }

    public void addEdge(Edge edge){
        if(!isThereEdge(edge)) {
            edges.add(edge);
        }
    }

    public void removeEdge(Edge edge){
        edges.remove(edge);
    }

    public void removePoint(Coord coord){
        while(getMinEdgeOf(coord) != null){
            removeEdge(getMinEdgeOf(coord));
        }
    }

    public boolean isThereCoord(Coord coord){
        for(Coord this_coord: points){
            if(this_coord.equals(coord)){
                return true;
            }
        }

        return false;
    }

    public boolean isThereEdge(Edge edge){
        return isThereEdge(edge.pointA, edge.pointB);
    }

    public boolean isThereEdge(Coord pointA, Coord pointB){
        for (Edge edge: edges) {
            if ((pointA.equals(edge.pointA) && pointB.equals(edge.pointB)) || (pointA.equals(edge.pointB) || pointB.equals(edge.pointA))) {
                return true;
            }
        }
        return false;
    }

    public Edge getMinEdgeOf(Coord coord){
        Edge minEdge = null;
        if(!isThereCoord(coord)) return null;
        for(Edge edge: edges){
            if(!edge.pointA.equals(coord) && !edge.pointB.equals(coord)) continue;
            if(minEdge == null || edge.value < minEdge.value){
                minEdge = edge;
            }
        }

        return minEdge;
    }

    public Edge getMinEdgeOfAll(){
        Edge minEdge = edges.get(0);
        for(Edge edge: edges){
            if(edge.value < minEdge.value){
                minEdge = edge;
            }
        }

        return minEdge;
    }

    public void generateMinEdgeOfAll(){
        for(Coord coord: points){
            generateMinEdgeOf(coord);
        }
    }

    public void generateMinEdgeOf(Coord c1){
        Edge edge = null;
        for (Coord c2 : points) {
            if(c1.equals(c2)) continue;
            if(edge == null || c1.distanceFrom(c2) < edge.value){
                edge = new Edge(c1, c2, c1.distanceFrom(c2));
            }
        }
        addEdge(edge);
    }
}