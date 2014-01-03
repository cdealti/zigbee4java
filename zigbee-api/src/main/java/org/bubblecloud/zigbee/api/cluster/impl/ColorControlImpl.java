/*
   Copyright 2008-2013 CNR-ISTI, http://isti.cnr.it
   Institute of Information Science and Technologies
   of the Italian National Research Council


   See the NOTICE file distributed with this work for additional
   information regarding copyright ownership

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package org.bubblecloud.zigbee.api.cluster.impl;

import org.bubblecloud.zigbee.api.ZigbeeDeviceException;
import org.bubblecloud.zigbee.api.cluster.general.ColorControl;
import org.bubblecloud.zigbee.api.cluster.general.Metering;
import org.bubblecloud.zigbee.api.cluster.impl.api.core.Attribute;
import org.bubblecloud.zigbee.api.cluster.impl.api.core.Reporter;
import org.bubblecloud.zigbee.api.cluster.impl.api.core.Status;
import org.bubblecloud.zigbee.api.cluster.impl.api.core.ZigBeeClusterException;
import org.bubblecloud.zigbee.api.cluster.impl.api.global.DefaultResponse;
import org.bubblecloud.zigbee.api.cluster.impl.general.ColorControlCluster;
import org.bubblecloud.zigbee.network.ZigbeeEndpoint;

/**
 * 
 * @author <a href="mailto:giancarlo.riolo@isti.cnr.it">Giancarlo Riolo</a>
 * @version $LastChangedRevision:  $ ($LastChangedDate: $)
 *
 */
public class ColorControlImpl implements ColorControl {
	
	private ColorControlCluster colorControlCluster;


	public ColorControlImpl(ZigbeeEndpoint zbDevice){
		colorControlCluster = new ColorControlCluster(zbDevice);
		
	}

	public int getId() {
		
		return colorControlCluster.getId();
	}

	public String getName() {
	
		return colorControlCluster.getName();
	}

    public Reporter[] getAttributeReporters() {
		return colorControlCluster.getAttributeReporters();
	}

	public Attribute[] getAttributes() {
	
		return colorControlCluster.getAvailableAttributes();
	}

	public Attribute getAttribute(int id) {
		Attribute[] attributes = colorControlCluster.getAvailableAttributes();
		for (int i = 0; i < attributes.length; i++) {
			if( attributes[i].getId() == id ) 
				return attributes[i];
		}
		return null;
	}

	public String getDescription() throws ZigbeeDeviceException {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public void moveToHue(short hue, byte direction, int transitionTime) throws ZigbeeDeviceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void moveHue(byte moveMode, short rate) throws ZigbeeDeviceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void stepHue(byte stepMode, short stepSize, short transtionTime) throws ZigbeeDeviceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void movetoSaturation(short saturation, int transitionTime) throws ZigbeeDeviceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void moveSaturation(byte moveMode, short rate) throws ZigbeeDeviceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void stepSaturation(byte stepMode, short stepSize, short transitionTime) throws ZigbeeDeviceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void movetoHue_Saturation(short hue, short saturation, int transitionTime) throws ZigbeeDeviceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void moveToColor(int colorX, int colorY, int transitionTime) throws ZigbeeDeviceException {
        try {
            DefaultResponse response = (DefaultResponse) colorControlCluster.moveToColor(colorX, colorY, transitionTime);
            if (!response.getStatus().equals(Status.SUCCESS))
                throw new ZigbeeDeviceException(response.getStatus().toString());
        } catch (ZigBeeClusterException e) {
            throw new ZigbeeDeviceException(e);
        }
    }

    @Override
    public void moveColor(int rateX, int rateY) throws ZigbeeDeviceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void stepColor(int stepX, int stepY, int transitionTime) throws ZigbeeDeviceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void moveToColorTemperature(int colorTemperature, int transitionTime) throws ZigbeeDeviceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }


}