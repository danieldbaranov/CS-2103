import java.util.*;
import java.util.function.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.sound.sampled.*;

public class ParticleSimulator extends JPanel {
	private Heap<Event> _events;
	private java.util.List<Particle> _particles;
	private double _duration;
	private int _width;
	final private Particle wall = new Particle("wall", 0, 0, 0, 0, 0);

	/**
	 * @param filename the name of the file to parse containing the particles
	 */
	public ParticleSimulator (String filename) throws IOException {
		_events = new HeapImpl<>();

		// Parse the specified file and load all the particles.
		Scanner s = new Scanner(new File(filename));
		_width = s.nextInt();
		_duration = s.nextDouble();
		s.nextLine();
		_particles = new ArrayList<>();
		while (s.hasNext()) {
			String line = s.nextLine();
			Particle particle = Particle.build(line);
			_particles.add(particle);
		}

		setPreferredSize(new Dimension(_width, _width));
	}

	@Override
	/**
	 * Draws all the particles on the screen at their current locations
	 * DO NOT MODIFY THIS METHOD
	 */
        public void paintComponent (Graphics g) {
		g.clearRect(0, 0, _width, _width);
		for (Particle p : _particles) {
			p.draw(g);
		}
	}

	// Helper class to signify the final event of the simulation.
	private class TerminationEvent extends Event {
		TerminationEvent (double timeOfEvent) {
			super(timeOfEvent, 0, null, null);
		}
	}

	/**
	 * Helper method to update the positions of all the particles based on their current velocities.
	 */
	private void updateAllParticles (double delta) {
		for (Particle p : _particles) {
			p.update(delta);
		}
	}

	/**
	 * Executes the actual simulation.
	 */
	private void simulate (boolean show) {
		double lastTime = 0;

		// Create initial events, i.e., all the possible
		// collisions between all the particles and each other,
		// and all the particles and the walls.
		for (Particle p : _particles) {
			addParticleCollisionTimeEvent(p, lastTime);
			addWallCollisionTimeEvent(p, lastTime);
		}

		_events.add(new TerminationEvent(_duration));
		while (_events.size() > 0) {
			Event event = _events.removeFirst();
			double delta = event._timeOfEvent - lastTime;

			if (event instanceof TerminationEvent) {
				updateAllParticles(delta);
				break;
			}

			// Check if event still valid; if not, then skip this event
			if (event._timeEventCreated > event._mainParticle.getlastUpdateTime() || event._timeEventCreated > event._otherParticle.getlastUpdateTime()) {
			   continue;
			 }


			// Since the event is valid, then pause the simulation for the right
			// amount of time, and then update the screen.
			if (show) {
				try {
					Thread.sleep((long) delta);
				} catch (InterruptedException ie) {}
			}

			// Update positions of all particles
			updateAllParticles(delta);

			// Update the velocity of the particle(s) involved in the collision
			// (either for a particle-wall collision or a particle-particle collision).
			// You should call the Particle.updateAfterCollision method at some point.

			event._mainParticle.updateAfterCollision(event._timeOfEvent, event._otherParticle, _width);
			//event._otherParticle.updateAfterCollision(event._timeOfEvent, event._mainParticle);

			// Enqueue new events for the particle(s) that were involved in this event.

			addParticleCollisionTimeEvent(event._mainParticle, event._timeOfEvent);
			addParticleCollisionTimeEvent(event._otherParticle, event._timeOfEvent);
			addWallCollisionTimeEvent(event._mainParticle, event._timeOfEvent);
			addWallCollisionTimeEvent(event._otherParticle, event._timeOfEvent);

			// Update the time of our simulation
			lastTime = event._timeOfEvent;

			// Redraw the screen
			if (show) {
				repaint();
			}
		}

		// Print out the final state of the simulation
		System.out.println(_width);
		System.out.println(_duration);
		for (Particle p : _particles) {
			System.out.println(p);
		}


	}

	private void addParticleCollisionTimeEvent(Particle mainParticle, double currentTime) {
		for (Particle p : _particles){
			if(!p.equals(mainParticle) && mainParticle.getCollisionTime(p) < Double.POSITIVE_INFINITY){
				_events.add(new Event(mainParticle.getCollisionTime(p) + currentTime, currentTime, mainParticle, p));
			}
		}
	}

	private void addWallCollisionTimeEvent(Particle p, double currentTime){
		Particle Yaxis = new Particle("Yaxis", 0, 0, 0, 0, 0);
		Particle Xaxis = new Particle("Xaxis", 0, 0, 0, 0, 0);

		if(p.getVX() > 0){ // Right Wall
			_events.add(new Event(p.getCollisionTimeTilWall(_width, "right"), currentTime, p, Xaxis));
		} else if (p.getVX() < 0){ // Left Wall
			_events.add(new Event(p.getCollisionTimeTilWall(_width, "left"), currentTime, p, Xaxis));
		}

		if(p.getVY() > 0){ // Bottom Wall
			_events.add(new Event(p.getCollisionTimeTilWall(_width, "bottom"), currentTime, p, Yaxis));
		} else if (p.getVY() < 0){ // Top Wall
			_events.add(new Event(p.getCollisionTimeTilWall(_width, "top"), currentTime, p, Yaxis));
		}
	}

	public static void main (String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("Usage: java ParticleSimulator <filename>");
			System.exit(1);
		}

		ParticleSimulator simulator;

		simulator = new ParticleSimulator(args[0]);
		JFrame frame = new JFrame();
		frame.setTitle("Particle Simulator");
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(simulator, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		simulator.simulate(true);
	}
}
