package com.elance.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import com.elance.nj4x.MT4ConnectionUtil;
import com.elance.util.OrderUtil;
import com.elance.util.constants.OrderAction;
import com.elance.vo.AccountConfig;
import com.elance.vo.AccountVO;
import com.jfx.ErrAccountDisabled;
import com.jfx.ErrCommonError;
import com.jfx.ErrCustomIndicatorError;
import com.jfx.ErrIntegerParameterExpected;
import com.jfx.ErrInvalidAccount;
import com.jfx.ErrInvalidFunctionParamvalue;
import com.jfx.ErrInvalidPrice;
import com.jfx.ErrInvalidPriceParam;
import com.jfx.ErrInvalidStops;
import com.jfx.ErrInvalidTradeParameters;
import com.jfx.ErrInvalidTradeVolume;
import com.jfx.ErrLongPositionsOnlyAllowed;
import com.jfx.ErrLongsNotAllowed;
import com.jfx.ErrMarketClosed;
import com.jfx.ErrNoConnection;
import com.jfx.ErrNotEnoughMoney;
import com.jfx.ErrOffQuotes;
import com.jfx.ErrOldVersion;
import com.jfx.ErrOrderLocked;
import com.jfx.ErrPriceChanged;
import com.jfx.ErrRequote;
import com.jfx.ErrServerBusy;
import com.jfx.ErrShortsNotAllowed;
import com.jfx.ErrStringParameterExpected;
import com.jfx.ErrTooFrequentRequests;
import com.jfx.ErrTooManyRequests;
import com.jfx.ErrTradeContextBusy;
import com.jfx.ErrTradeDisabled;
import com.jfx.ErrTradeExpirationDenied;
import com.jfx.ErrTradeModifyDenied;
import com.jfx.ErrTradeNotAllowed;
import com.jfx.ErrTradeTimeout;
import com.jfx.ErrTradeTimeout2;
import com.jfx.ErrTradeTimeout3;
import com.jfx.ErrTradeTimeout4;
import com.jfx.ErrTradeTooManyOrders;
import com.jfx.ErrUnknownSymbol;
import com.jfx.MarketInfo;
import com.jfx.TradeOperation;

public class OrderListener implements ActionListener {
	
	private OrderAction orderAction;
	private List<AccountVO> accountList;
	private AccountConfig accountConfig;
	
	public OrderListener(){
	}
	
	public OrderListener(OrderAction orderAction,List<AccountVO> accountList,AccountConfig accountConfig){
		this.orderAction=orderAction;
		this.accountList=accountList;
		this.accountConfig=accountConfig;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
				
      for(AccountVO accountVO:accountList){
    	  
    	  System.out.println("=========accountVO:\t"+accountVO.getAccountText().getText()+"\t"+orderAction);
    	  
    	  if(accountVO.getTotalLotsForNextTrade()==0.0){
    		  continue;
    	  }
    	  
    	  String maxLotsStr=accountConfig.getMaxLotsSpinner().getValue().toString();
    	  MT4ConnectionUtil mt4Util=accountVO.getMt4ConnectionUtil();
    	  String symbol=mt4Util.getSymbol();
    	  
    	  double[] lots=OrderUtil.calculateOrderLots(accountVO.getTotalLotsForNextTrade(),Double.valueOf(maxLotsStr));
		  int maxTradeLots=Integer.parseInt(accountConfig.getMaxTradesSpinner().getValue().toString());
		  int num=0;
    	  try {
				for (double tradeLot : lots) {
					
					num++;
					if(num>maxTradeLots){
						continue;
					}
					
					if(orderAction==OrderAction.OPEN_BUY){
						mt4Util.orderSend(symbol, TradeOperation.OP_BUY, tradeLot,mt4Util.marketInfo(symbol, MarketInfo.MODE_ASK), 10, 0,0, null, 0, null);
					}else if(orderAction==OrderAction.OPEN_SELL){
						mt4Util.orderSend(symbol, TradeOperation.OP_SELL, tradeLot,mt4Util.marketInfo(symbol, MarketInfo.MODE_BID), 10, 0,0, null, 0, null);
					}
			
				}
			} catch (ErrInvalidFunctionParamvalue | ErrCustomIndicatorError
					| ErrStringParameterExpected | ErrIntegerParameterExpected
					| ErrUnknownSymbol | ErrInvalidPriceParam
					| ErrTradeNotAllowed | ErrLongsNotAllowed
					| ErrShortsNotAllowed | ErrCommonError
					| ErrInvalidTradeParameters | ErrServerBusy | ErrOldVersion
					| ErrNoConnection | ErrTooFrequentRequests
					| ErrAccountDisabled | ErrInvalidAccount | ErrTradeTimeout
					| ErrInvalidPrice | ErrInvalidStops | ErrInvalidTradeVolume
					| ErrMarketClosed | ErrTradeDisabled | ErrNotEnoughMoney
					| ErrPriceChanged | ErrOffQuotes | ErrRequote
					| ErrOrderLocked | ErrLongPositionsOnlyAllowed
					| ErrTooManyRequests | ErrTradeTimeout2 | ErrTradeTimeout3
					| ErrTradeTimeout4 | ErrTradeModifyDenied
					| ErrTradeContextBusy | ErrTradeExpirationDenied
					| ErrTradeTooManyOrders ex) {
				ex.printStackTrace();
			}
      }   
	}

}