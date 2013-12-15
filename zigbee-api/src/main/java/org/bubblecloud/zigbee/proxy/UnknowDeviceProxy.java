/*
   Copyright 2013-2013 CNR-ISTI, http://isti.cnr.it
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

package org.bubblecloud.zigbee.proxy;

import org.bubblecloud.zigbee.ZigbeeContext;
import org.bubblecloud.zigbee.network.ZigBeeDevice;

/**
 *
 * @author <a href="mailto:stefano.lenzi@isti.cnr.it">Stefano "Kismet" Lenzi</a>
 * @version $LastChangedRevision: 799 $ ($LastChangedDate: 2013-08-06 19:00:05 +0300 (Tue, 06 Aug 2013) $)
 * @since 0.7.0
 *
 */
public class UnknowDeviceProxy extends DeviceProxyBase {

    public UnknowDeviceProxy(ZigbeeContext ctx, ZigBeeDevice zbDevice) throws ZigBeeHAException {
        super(ctx,zbDevice);
        int[] inputClusters = zbDevice.getInputClusters();
        for (int i = 0; i < inputClusters.length; i++) {
            addCluster( inputClusters[i] );
        }

        int[] outputClusters = zbDevice.getOutputClusters();
        for (int i = 0; i < inputClusters.length; i++) {
            addCluster( outputClusters[i] );
        }
    }

    @Override
    public String getName() {
        return "Unknow HA Device";
    }

    final static DeviceDescription DEVICE_DESCRIPTOR =  new AbstractDeviceDescription(){

        public int[] getCustomClusters() {
            return new int[]{};
        }

        public int[] getMandatoryCluster() {
            return MANDATORY;
        }

        public int[] getOptionalCluster() {
            return OPTIONAL;
        }

        public int[] getStandardClusters() {
            return STANDARD;
        }

    };

    @Override
    public DeviceDescription getDescription() {
        return DEVICE_DESCRIPTOR;
    }


}