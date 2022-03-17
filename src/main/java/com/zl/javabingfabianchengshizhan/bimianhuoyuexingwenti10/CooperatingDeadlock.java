package com.zl.javabingfabianchengshizhan.bimianhuoyuexingwenti10;

import com.zl.javabingfabianchengshizhan.threadSafe.Point;
import io.netty.util.internal.ConcurrentSet;
import net.jcip.annotations.GuardedBy;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Allen.zhang
 * @title: CooperatingDeadlock
 * @projectName zl
 * @description: TODO
 * @date 2022/3/713:28
 */
public class CooperatingDeadlock {


    class Taxi {

        @GuardedBy("this")
        private Point location, destination;
        private final Dispatcher dispatcher;


        Taxi(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        public synchronized Point getLocation() {
            return location;
        }

        public synchronized void setLocation(Point location) {
            this.location = location;
            if (location == destination) {
                dispatcher.notifyAvailable(this);
            }
        }

        public synchronized Point getDestination() {
            return destination;
        }

        public synchronized void setDestination(Point destination) {
            this.destination = destination;
        }

        public Dispatcher getDispatcher() {
            return dispatcher;
        }
    }

    class Dispatcher {

        @GuardedBy("this")
        private final Set<Taxi> taxis;

        @GuardedBy("this")
        private final Set<Taxi> availableTaxis;

        public Dispatcher() {
            taxis = new ConcurrentSet<>();
            availableTaxis = new ConcurrentSet<>();
        }

        public synchronized Image getImage(){
            Image image = new Image();
            taxis.forEach(e -> image.drawMarker(e.getLocation()));
            return image;
        }




        public synchronized void notifyAvailable(Taxi taxi) {
            availableTaxis.add(taxi);
        }
    }

    class Image {
        public void drawMarker(Point point){}
    }


}
