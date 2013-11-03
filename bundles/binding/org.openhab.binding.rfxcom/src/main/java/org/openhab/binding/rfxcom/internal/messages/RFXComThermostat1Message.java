/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.rfxcom.internal.messages;

/**
import org.openhab.binding.rfxcom.internal.RFXComBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
*/

/**
 * RFXCOM data class for thermostat message.
 * 
 * @author Pauli Anttila, Gilles Bastié
 * @since 1.2.0
 */
public class RFXComThermostat1Message extends RFXComBaseMessage {
/**
	private static final Logger logger = LoggerFactory
			.getLogger(RFXComBinding.class);
*/
	public enum SubType {
		DIGIMAX(0),
		DIGIMAX_SHORT_FORMAT(1),

		UNKNOWN(255);

		private final int subType;

		SubType(int subType) {
			this.subType = subType;
		}

		SubType(byte subType) {
			this.subType = subType;
		}

		public byte toByte() {
			return (byte) subType;
		}
	}

	public enum ThermostatStatus {
		NO_STATUS(0),
		DEMAND(1),
		NO_DEMAND(2),
		INITIALIZING(3),

		UNKNOWN(255);

		private final int thermostatStatus;

		ThermostatStatus(int thermostatStatus) {
			this.thermostatStatus = thermostatStatus;
		}

		ThermostatStatus(byte thermostatStatus) {
			this.thermostatStatus = thermostatStatus;
		}

		public byte toByte() {
			return (byte) thermostatStatus;
		}
	}

	public enum ThermostatMode {
		HEATING(0),
		COOLING(1),

		UNKNOWN(255);

		private final int thermostatMode;

		ThermostatMode(int thermostatMode) {
			this.thermostatMode = thermostatMode;
		}

		ThermostatMode(byte thermostatMode) {
			this.thermostatMode = thermostatMode;
		}

		public byte toByte() {
			return (byte) thermostatMode;
		}
	}

	public SubType subType = SubType.UNKNOWN;
	public int sensorId = 0;
	public int temperature = 0;
	public int temperatureSet = 0;
	public ThermostatStatus thermostatStatus = ThermostatStatus.UNKNOWN;
	public ThermostatMode thermostatMode = ThermostatMode.UNKNOWN;
	public byte signalLevel = 0;

	public RFXComThermostat1Message() {
		packetType = PacketType.THERMOSTAT1;
	}

	public RFXComThermostat1Message(byte[] data) {

		encodeMessage(data);
	}

	@Override
	public String toString() {
		String str = "";

		str += super.toString();
		str += "\n - Sub type = " + subType;
		str += "\n - Id = " + sensorId;
		str += "\n - Temperature = " + temperature;
		str += "\n - Temperature set = " + temperatureSet;
		str += "\n - Mode = " + thermostatMode;
		str += "\n - Status = " + thermostatStatus;
		str += "\n - Signal level = " + signalLevel;

		return str;
	}

	@Override
	public void encodeMessage(byte[] data) {
//		logger.debug("GBA: RFXComThermostat1Message.encodeMessage");
				
		super.encodeMessage(data);


		try {
			subType = SubType.values()[super.subType];
		} catch (Exception e) {
			subType = SubType.UNKNOWN;
		}		
		sensorId = (data[4] & 0xFF) << 8 | (data[5] & 0xFF);

		temperature = data[6];
		temperatureSet = data[7];

/**		
		logger.debug("GBA: RFXComThermostat1Message.encodeMessage: subType: {}", subType.toString());
		logger.debug("GBA: RFXComThermostat1Message.encodeMessage: sensorId: {}", sensorId);
		logger.debug("GBA: RFXComThermostat1Message.encodeMessage: temperature: {}", temperature);
		logger.debug("GBA: RFXComThermostat1Message.encodeMessage: temperatureSet: {}", temperatureSet);
*/
		try {
			thermostatStatus = ThermostatStatus.values()[data[8] & 0x03];
		} catch (Exception e) {
			thermostatStatus = ThermostatStatus.UNKNOWN;
		}
//		logger.debug("GBA: RFXComThermostat1Message.encodeMessage: thermostatStatus: {}", thermostatStatus.toString());

		try {
			thermostatMode = ThermostatMode.values()[data[8] & 0x80];
		} catch (Exception e) {
			thermostatMode = ThermostatMode.UNKNOWN;
		}
//		logger.debug("GBA: RFXComThermostat1Message.encodeMessage: thermostatMode: {}", thermostatMode.toString());

		signalLevel = (byte) ((data[9] & 0xF0) >> 4);
//		logger.debug("GBA: RFXComThermostat1Message.encodeMessage: signalLevel: {}", signalLevel);
	}

	@Override
	public byte[] decodeMessage() {
//		logger.debug("GBA: RFXComThermostat1Message.decodeMessage");

		byte[] data = new byte[10];

		data[0] = 0x09;
		data[1] = RFXComBaseMessage.PacketType.THERMOSTAT1.toByte();
		data[2] = subType.toByte();
		data[3] = seqNbr;
		data[4] = (byte) ((sensorId & 0xFF00) >> 8);
		data[5] = (byte) (sensorId & 0x00FF);

		data[6] = (byte) temperature;
		data[7] = (byte) temperatureSet;

		data[8] = (byte) (thermostatStatus.toByte() + thermostatMode.toByte());
		data[9] = (byte) ((signalLevel & 0x0F) << 4);

		return data;
	}
	
	@Override
	public String generateDeviceId() {
		 return String.valueOf(sensorId);
	}

	
	/*
	public Byte decodeSignal(byte sigLevel) {
		 return String.valueOf(sensorId);
	}
	*/

}
