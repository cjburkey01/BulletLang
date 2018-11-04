package com.cjburkey.bullet.obj;

import com.cjburkey.bullet.obj.statement.BStatement;
import java.util.List;

/**
 * Created by CJ Burkey on 2018/11/03
 */
public interface IBScope {
    
    List<BStatement> getStatements();
    
}
