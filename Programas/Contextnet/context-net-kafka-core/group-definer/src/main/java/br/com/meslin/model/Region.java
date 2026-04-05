package br.com.meslin.model;

import java.util.ArrayList;
import java.util.List;

import org.openstreetmap.gui.jmapviewer.Coordinate;

/**
 * Define uma região e métodos de acesso e verificação.
 * <p>
 * A região pode ser concava ou convexa
 * 
 * @author meslin
 *
 */
public class Region
{
	private List<Coordinate> points;
	private int number;

	/**
	 * Constroi uma região vazia
	 */
	public Region()
	{
		super();
		points = new ArrayList<Coordinate>();
	}
	
	public void setNumber(int numero) { this.number = numero; }
	public int getNumber() { return this.number; }

	/**
	 * Adiciona um ponto à região
	 * 
	 * @param point
	 */
	public void add(Coordinate point)
	{
		this.points.add(point);
	}
	public List<Coordinate> getPoints()
	{
		return this.points;
	}
	
	/**
	 * Verifica se o ponto pertence a região
	 * 
	 * @param coordinates
	 * @return verdadeiro se o ponto pertencer à região
	 */
	public boolean contains(Coordinate coordinates)
	{
		boolean result = false;
		
		/*
		 * para todo segmento de reta da região cuja reta cruza a linha y do ponto,
		 * se a soma dos pontos x menores do que a posição x do ponto for ímpar,
		 * o ponto estará dentro da região
		 */
		for(int i=0, j=this.points.size()-1; i<this.points.size(); j=i++)
		{
			if(((this.points.get(i).getLat() > coordinates.getLat()) != (this.points.get(j).getLat() > coordinates.getLat()))
			&& (coordinates.getLon() < ((this.points.get(j).getLon()-this.points.get(i).getLon()) * (coordinates.getLat()-this.points.get(i).getLat()) / (this.points.get(j).getLat()-this.points.get(i).getLat()) + this.points.get(i).getLon())))
				result = !result;
		}
		return result;
	}

	public boolean contains(MobileObject coordinates)
	{
		return contains(new Coordinate(coordinates.getLatitude(), coordinates.getLongitude()));
	}
	
	/**
	 * getRegionCenter
	 * @return the coordinate of the center of the region
	 */
	public Coordinate getRegionCenter() {
		double lat = 0;
		double lon = 0;
		double latMin = 1000;
		double latMax = -1000;
		double lonMin = 1000;
		double lonMax = -1000;
		for (Coordinate coordinate : points) {
			lat += coordinate.getLat();
			lon += coordinate.getLon();
			if(coordinate.getLat()>latMax) {
				latMax = coordinate.getLat();
			}
			else if(coordinate.getLat()<latMin) {
				latMin = coordinate.getLat();
			}
			if(coordinate.getLon()>lonMax) {
				lonMax = coordinate.getLon();
			}
			else if(coordinate.getLon()<lonMin) {
				lonMin = coordinate.getLon();
			}
		}
		lat /= points.size();
		lon /= points.size();
		if(contains(new Coordinate(lat, lon))) return new Coordinate(lat, lon);
		else return new Coordinate(latMin + (latMax - latMin)/2, lonMin + (lonMax - lonMin)/2);
	}
}
