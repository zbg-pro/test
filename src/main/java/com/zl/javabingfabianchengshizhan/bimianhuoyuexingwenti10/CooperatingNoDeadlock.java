package com.zl.javabingfabianchengshizhan.bimianhuoyuexingwenti10;

import com.zl.javabingfabianchengshizhan.threadSafe.Point;
import io.netty.util.internal.ConcurrentSet;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Allen.zhang
 * @title: CooperatingNoDeadlock
 * @projectName zl
 * @description: TODO
 * @date 2022/3/717:42
 */
public class CooperatingNoDeadlock {
    @ThreadSafe
    class Taxi{
        @GuardedBy("this") private Point location, destination;
        private final Dispatcher dispatcher;

        public Taxi(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        public synchronized Point getLocation() {
            return location;
        }
        
        public synchronized void setLocation(Point location){
            boolean reachedDestination;
            synchronized (this) {
                this.location = location;
                reachedDestination = location.equals(destination);
            }
            
            if (reachedDestination)
            dispatcher.notifyAvailable(this);
        }

        public synchronized Point getDestination() {
            return destination;
        }

        public synchronized void setDestination(Point destination) {
            this.destination = destination;
        }
    }
    
    @ThreadSafe
    class Dispatcher{
        @GuardedBy("this") private final Set<Taxi> taxis;
        @GuardedBy("this") private final Set<Taxi> availableTaxis;

        public Dispatcher() {
            taxis = new HashSet<Taxi>();
            availableTaxis = new HashSet<Taxi>();
        }

        public synchronized void notifyAvailable(Taxi taxi) {
            availableTaxis.add(taxi);
        }
        
        public Image getImage(){
            Set<Taxi> copy;
            synchronized (this) {
                copy = new HashSet<>(taxis);
            }

            Image image = new Image();
            for (Taxi t : copy)
                image.drawMarker(t.getLocation());
            return image;
        }
        
    }
    
    @ThreadSafe
    class Image{
        public void drawMarker(Point location) {
        }
    }
    
    
}
